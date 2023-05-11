package com.ajudaqui.controle.de.pagamentos30.validacao;

import java.time.LocalDate;

import com.ajudaqui.controle.de.pagamentos30.entity.Boleto;
import com.ajudaqui.controle.de.pagamentos30.entity.StatusBoleto;

public class StatusBoletoAVencer extends Status{

	public StatusBoletoAVencer(Status proximo) {
		super(proximo);
		// TODO Auto-generated constructor stub
	}

	@Override
	public StatusBoleto validar(Boleto boleto) {
		if(LocalDate.now().plusDays(7).isAfter(boleto.getVencimento())) {
			return StatusBoleto.A_VENCER;
		}
		return proximo.validar(boleto);
	}

}
