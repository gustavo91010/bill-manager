package com.ajudaqui.bill.manager.validacao;

import java.time.LocalDate;

import com.ajudaqui.bill.manager.entity.Payment;
import com.ajudaqui.bill.manager.entity.StatusBoleto;

public class StatusBoletoVencendoHoje extends Status {

	private LocalDate hoje= LocalDate.now();
	public StatusBoletoVencendoHoje(Status proximo) {
		super(proximo);
		// TODO Auto-generated constructor stub
	}

	@Override
	public StatusBoleto validar(Payment boleto) {
		boolean vencendo_hoje = boleto.getDue_date().compareTo(hoje) == 0;
		if(vencendo_hoje) {
			return StatusBoleto.VENCENDO_HOJE;
		}
		return proximo.validar(boleto);
	}
	

}
