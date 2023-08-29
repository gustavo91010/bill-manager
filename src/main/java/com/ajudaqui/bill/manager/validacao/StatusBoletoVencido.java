package com.ajudaqui.bill.manager.validacao;

import java.time.LocalDate;

import com.ajudaqui.bill.manager.entity.Payment;
import com.ajudaqui.bill.manager.entity.StatusBoleto;

public class StatusBoletoVencido extends Status {
	private LocalDate hoje = LocalDate.now();

	public StatusBoletoVencido(Status proximo) {
		super(proximo);
	}

	@Override
	public StatusBoleto validar(Payment boleto) {
		boolean vencido = boleto.getDue_date().compareTo(hoje) < 0;
		if (vencido) {
			return StatusBoleto.VENCIDO;
		}
		return proximo.validar(boleto);
	}

}
