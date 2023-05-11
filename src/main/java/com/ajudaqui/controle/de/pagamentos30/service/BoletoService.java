package com.ajudaqui.controle.de.pagamentos30.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ajudaqui.controle.de.pagamentos30.dto.BoletoDto;
import com.ajudaqui.controle.de.pagamentos30.entity.Boleto;
import com.ajudaqui.controle.de.pagamentos30.entity.ValidarStatus;
import com.ajudaqui.controle.de.pagamentos30.entity.Vo.BoletoVO;
import com.ajudaqui.controle.de.pagamentos30.from.BoletoFrom;
import com.ajudaqui.controle.de.pagamentos30.repository.BoletoRepository;
import com.ajudaqui.controle.de.pagamentos30.specification.BoletoSpecification;

@Service
public class BoletoService {
	
	@Autowired
	private BoletoRepository boletoRepository;

	public Boleto cadastrar(BoletoDto boletoDto) {

		return boletoRepository.save( boletoDto.toDatabase());
		
	}

	public List<BoletoVO> findAll(BoletoFrom boletoFrom) {
		Specification<Boleto> spefications = Specification
				.where(BoletoSpecification.descricao(boletoFrom.getDescricao())
				);
		
		List<Boleto> boletos = boletoRepository.findAll(spefications);
		 atualizarStatus(boletos);
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

	public List<Boleto> findByDescricao(String descricao) {
		List<Boleto> boletos = boletoRepository.findByDescricao(descricao);
		
		
		 return boletos;
	}
	
	public List<Boleto> atualizarStatus(List<Boleto> boletos) {
		ValidarStatus validarStatus = new ValidarStatus();
		boletos.forEach(b -> {
			validarStatus.statusAtualizado(b, boletoRepository);

		});
		return boletos;

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

}
