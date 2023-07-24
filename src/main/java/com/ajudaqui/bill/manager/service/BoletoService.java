package com.ajudaqui.bill.manager.service;

import static java.time.LocalDate.now;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ajudaqui.bill.manager.dto.BoletoDto;
import com.ajudaqui.bill.manager.dto.EmailDto;
import com.ajudaqui.bill.manager.entity.Boleto;
import com.ajudaqui.bill.manager.entity.StatusBoleto;
import com.ajudaqui.bill.manager.entity.Vo.BoletoVO;
import com.ajudaqui.bill.manager.from.BoletoFrom;
import com.ajudaqui.bill.manager.http.EmailClient;
import com.ajudaqui.bill.manager.repository.BoletoRepository;
import com.ajudaqui.bill.manager.specification.BoletoSpecification;
import com.ajudaqui.bill.manager.utils.Xlsx;

@Service
public class BoletoService {
	
	@Autowired
	private BoletoRepository boletoRepository;
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private EmailClient emailClient;
	
	public Boleto pagamento(Long id) {
		Boleto boleto = findById(id);
		boleto.setStatus(StatusBoleto.PAGO);
		boletoRepository.save(boleto);
		return boleto;
		
	}
	public void boletosRecorrentes(BoletoDto boletoDto, Long repeticao) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		
		for(int i=0;i< repeticao; i++ ) {
			
			LocalDate vencimento=LocalDate.parse(boletoDto.getVencimento(), formatter);
			boletoDto.setVencimento(vencimento.plusMonths(i).toString());
			cadastrar(boletoDto);
		}
		
	}
	public static void main(String[] args) {
		System.out.println(LocalDate.now());
	}

	public Boleto cadastrar(BoletoDto boletoDto) {

		return boletoRepository.save( boletoDto.toDatabase(boletoRepository));
		
	}

	public List<BoletoVO> findAll(BoletoFrom boletoFrom) {
		Specification<Boleto> spefications = Specification
				.where(BoletoSpecification.descricao(boletoFrom.getDescricao())
				);
		
		List<Boleto> boletos = boletoRepository.findAll(spefications);
//		 atualizarStatus(boletos);
			List<BoletoVO> boletosVO = new ArrayList<>();
			boletos.forEach(b -> {
				boletosVO.add(new BoletoVO(b));
			});
		 
		 return boletosVO;
	}

	public Boleto findById(Long id) {

		Boleto boleto = boletoRepository.findById(id)
				.orElseThrow(()->  new RuntimeException("Boleto não encontrado."));
		return boleto;
	}

	public List<BoletoVO> findByDescricao(String descricao) {
		List<Boleto> boletos = boletoRepository.findByDescricao(descricao);
		List<BoletoVO> boletosVO = new ArrayList<>();
		boletos.forEach(b -> {
			boletosVO.add(new BoletoVO(b));
		});
		
		
		 return boletosVO;
	}


	public List<BoletoVO> findBoletosPagos(int mes,int ano) {
		LocalDate inicio= LocalDate.of(ano, mes, 1);
		LocalDate fim= inicio.plusMonths(1).minusDays(1);

		List<Boleto> b = boletoRepository.findBoletosPagos(inicio, fim);
		
		List<BoletoVO> boletosVO = new ArrayList<>();
		b.forEach(bo -> {
			boletosVO.add(new BoletoVO(bo));
		});
		return boletosVO;
	}

	public List<BoletoVO> findBoletosVencidos() {
		List<Boleto> boletos = boletoRepository.findBoletosVencidos();
		
		List<BoletoVO> boletosVO = new ArrayList<>();
		boletos.forEach(bo -> {
			boletosVO.add(new BoletoVO(bo));
		});
		return boletosVO;
	}

	public List<BoletoVO> findBoletosDoMes(int mes, int ano) {
		LocalDate inicio= LocalDate.of(ano, mes, 1);
		LocalDate fim= inicio.plusMonths(1).minusDays(1);
		
		List<Boleto> boletos = boletoRepository.findBoletosDoMes(inicio,fim);
		List<BoletoVO> boletosVO = new ArrayList<>();

		boletos.forEach(b -> {
			boletosVO.add(new BoletoVO(b));
		});
		return boletosVO;
	}

	public List<BoletoVO> findBoletosASeremPagosNoMes(int mes, int ano) {
		LocalDate inicio= LocalDate.of(ano, mes, 1);
		LocalDate fim= inicio.plusMonths(1).minusDays(1);
		
		List<Boleto> boletos = boletoRepository.findBoletosASeremPagosNoMes(inicio,fim);
		List<BoletoVO> boletosVO = new ArrayList<>();

		boletos.forEach(b -> {
			boletosVO.add(new BoletoVO(b));
		});
		return boletosVO;
	}

	public void deleteById(Long id) {
		Boleto boleto = findById(id);
		boletoRepository.delete(boleto);
		
	}
	//actualização do estado em execução
	public void performStatusUpdate() {
		List<Boleto> pagamentos = boletoRepository.nextPayments(LocalDate.now().plusDays(10));
		atualizarStatus(pagamentos);
	}
	
	public List<Boleto> atualizarStatus(List<Boleto> boletos) {
		boletos.forEach(b -> {
			ValidarStatus.statusAtualizado(b, boletoRepository);

		});
		return boletos;

	}
	
	public void resumoDoMesXlsx(String nome) throws IOException {
		List<Boleto> boletos= new ArrayList<>();
		
		List<BoletoVO> boletosVO = findBoletosDoMes(now().getMonthValue(), now().getYear());
		boletosVO.forEach(b -> {
			boletos.add(modelMapper.map(b, Boleto.class));
		});
		Xlsx.planilhaBoletos(boletos, nome);
		EmailDto email= new EmailDto();
		email.setEmailTo("gustavo91010@gmail.com");
		email.setSubject("testando micro servicos");
		email.setText("opa lel, opa lala, ta na hora de pegar!");
		email.setUser_id(1l);
		
		emailClient.sendingEmail(email);
	System.err.println(email.toString());
	}

}
