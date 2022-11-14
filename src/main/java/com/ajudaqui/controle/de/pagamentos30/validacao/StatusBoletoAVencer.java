package com.ajudaqui.controle.de.pagamentos30.validacao;

import com.ajudaqui.controle.de.pagamentos30.entity.Boleto;
import com.ajudaqui.controle.de.pagamentos30.entity.StatusBoleto;

public class StatusBoletoAVencer extends Status {
//	private LocalDate hoje = LocalDate.now();

//	public StatusBoletoAVencer(Status proximo) {
//		super(proximo);
//	}
	public StatusBoletoAVencer() {
		super(null);
	}

	@Override
	public StatusBoleto validar(Boleto boleto) {
		return StatusBoleto.A_VENCER;
//		boolean a_vencer = boleto.getVencimento().compareTo(hoje) > 0;
//		if (a_vencer) {
//			return StatusBoleto.A_VENCER;
//
//		}
//		return proximo.validar(boleto);
	}

}
