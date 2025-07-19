package com.ajudaqui.billmanager.utils.validacao;

import java.time.LocalDate;

import com.ajudaqui.billmanager.entity.Payment;
import com.ajudaqui.billmanager.utils.StatusBoleto;

public class StatusBoletoVencendoHoje extends Status {

	private LocalDate hoje= LocalDate.now();
	public StatusBoletoVencendoHoje(Status proximo) {
		super(proximo);
	}

	@Override
	public StatusBoleto validar(Payment boleto) {
		boolean vencendoHoje = boleto.getDueDate().compareTo(hoje) == 0;
		if(vencendoHoje) {
			return StatusBoleto.VENCENDO_HOJE;
		}
		return proximo.validar(boleto);
	}
	

}
