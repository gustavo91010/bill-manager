package com.ajudaqui.bill.manager.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ajudaqui.bill.manager.dto.EmailDto;
import com.ajudaqui.bill.manager.dto.PayamentDto;
import com.ajudaqui.bill.manager.entity.Payment;
import com.ajudaqui.bill.manager.entity.StatusBoleto;
import com.ajudaqui.bill.manager.entity.Users;
import com.ajudaqui.bill.manager.entity.Vo.PayamentVO;
import com.ajudaqui.bill.manager.from.BoletoFrom;
import com.ajudaqui.bill.manager.http.EmailClient;
import com.ajudaqui.bill.manager.repository.PaymentsRepository;
import com.ajudaqui.bill.manager.specification.BoletoSpecification;
import com.ajudaqui.bill.manager.utils.Xlsx;

@Service
public class PayamentService {
	
	@Autowired
	private PaymentsRepository boletoRepository;
	@Autowired
	private UsersService usersService;
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private EmailClient emailClient;
	
	
	public Payment pagamento(Long id) {
		Payment boleto = findById(id);
		boleto.setStatus(StatusBoleto.PAGO);
		boletoRepository.save(boleto);
		return boleto;
		
	}
	public void boletosRecorrentes(PayamentDto boletoDto, Long repeticao) {
		
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		
		for(int i=0;i< repeticao; i++ ) {
			
			LocalDate vencimento=LocalDate.parse(boletoDto.getVencimento(), formatter);
			boletoDto.setVencimento(vencimento.plusMonths(i).toString());
			cadastrar(boletoDto);
		}
		
	}

	public Payment cadastrar(PayamentDto payamentDto) {
		Users users = usersService.findById(payamentDto.getUsersId());

		return boletoRepository.save( payamentDto.toDatabase(boletoRepository,users));
		
	}

	public List<PayamentVO> findAll(BoletoFrom boletoFrom) {
		Specification<Payment> spefications = Specification
				.where(BoletoSpecification.descricao(boletoFrom.getDescricao())
				);
		
		List<Payment> boletos = boletoRepository.findAll(spefications);
//		 atualizarStatus(boletos);
			List<PayamentVO> boletosVO = new ArrayList<>();
			boletos.forEach(b -> {
				boletosVO.add(new PayamentVO(b));
			});
		 
		 return boletosVO;
	}

	public Payment findById(Long id) {

		Payment boleto = boletoRepository.findById(id)
				.orElseThrow(()->  new RuntimeException("Boleto não encontrado."));
		return boleto;
	}
	public List<Payment> findByPayamentsForUser(Long userId) {
		return boletoRepository.findByPayamentsForUser(userId);
		
		
	}

	public List<PayamentVO> findByDescricao(String descricao) {
		List<Payment> boletos =new ArrayList<>();
//				boletoRepository.findByDescricao(descricao);
		List<PayamentVO> boletosVO = new ArrayList<>();
		boletos.forEach(b -> {
			boletosVO.add(new PayamentVO(b));
		});
		
		
		 return boletosVO;
	}






	public void deleteById(Long id) {
		Payment boleto = findById(id);
		boletoRepository.delete(boleto);
		
	}
	//actualização do estado em execução
	public void performStatusUpdate() {
		List<Payment> pagamentos = boletoRepository.nextPayments(LocalDate.now().plusDays(10));
		atualizarStatus(pagamentos);
	}
	
	public List<Payment> atualizarStatus(List<Payment> boletos) {
		boletos.forEach(b -> {
			ValidarStatus.statusAtualizado(b, boletoRepository);

		});
		return boletos;

	}
	
	public void resumoDoMesXlsx(String nome) throws IOException {
		List<Payment> boletos= new ArrayList<>();
		
		List<PayamentVO> boletosVO =new ArrayList<>();
//				findBoletosDoMes(now().getMonthValue(), now().getYear());
		boletosVO.forEach(b -> {
			boletos.add(modelMapper.map(b, Payment.class));
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