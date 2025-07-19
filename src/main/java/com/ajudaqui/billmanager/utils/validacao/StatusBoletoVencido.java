package com.ajudaqui.billmanager.utils.validacao;

import java.time.LocalDate;

import com.ajudaqui.billmanager.entity.Payment;
import com.ajudaqui.billmanager.utils.StatusBoleto;

public class StatusBoletoVencido extends Status {
	private LocalDate hoje = LocalDate.now();

	public StatusBoletoVencido(Status proximo) {
		super(proximo);
	}

	@Override
	public StatusBoleto validar(Payment boleto) {
		boolean vencido = boleto.getDueDate().compareTo(hoje) < 0;
		if (vencido) {
			return StatusBoleto.VENCIDO;
		}
		return proximo.validar(boleto);
	}

}
