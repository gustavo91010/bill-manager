package com.ajudaqui.controle.de.pagamentos30.service;

import com.ajudaqui.controle.de.pagamentos30.entity.Boleto;
import com.ajudaqui.controle.de.pagamentos30.repository.BoletoRepository;
import com.ajudaqui.controle.de.pagamentos30.validacao.Status;
import com.ajudaqui.controle.de.pagamentos30.validacao.StatusBoletoAVencer;
import com.ajudaqui.controle.de.pagamentos30.validacao.BoletosEmDias;
import com.ajudaqui.controle.de.pagamentos30.validacao.StatusBoletoPago;
import com.ajudaqui.controle.de.pagamentos30.validacao.StatusBoletoVencendoHoje;
import com.ajudaqui.controle.de.pagamentos30.validacao.StatusBoletoVencido;

public class ValidarStatus {

	public static Boleto statusAtualizado(Boleto boleto, BoletoRepository repository) {
		
		Status status= new StatusBoletoPago(
						   new StatusBoletoVencido(
							   new StatusBoletoVencendoHoje(
								   new StatusBoletoAVencer(
										new BoletosEmDias()))));
		
		boleto.setStatus(status.validar(boleto));
		return repository.save(boleto);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	// A_VENCER, VENCIDO, VENCENDO_HOJE
//		LocalDate hoje = LocalDate.now();
//		boolean a_vencer = boleto.getVencimento().compareTo(hoje) > 0;
//		boolean vencido = boleto.getVencimento().compareTo(hoje) < 0;
//		boolean vencendo_hoje = boleto.getVencimento().compareTo(hoje) == 0;
//		
//		if (boleto.getStatus() != StatusBoleto.PAGO) {
//
//			if (a_vencer) {
//				boleto.setStatus(StatusBoleto.A_VENCER);
//
//			}
//			if (vencido) {
//				boleto.setStatus(StatusBoleto.VENCIDO);
//			}
//			if (vencendo_hoje)
//				boleto.setStatus(StatusBoleto.VENCENDO_HOJE);
//		}
//		return repository.save(boleto);
//	}

}
