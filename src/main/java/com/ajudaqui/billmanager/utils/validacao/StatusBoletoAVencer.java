package com.ajudaqui.billmanager.utils.validacao;

import java.time.LocalDate;

import com.ajudaqui.billmanager.entity.Payment;
import com.ajudaqui.billmanager.utils.StatusBoleto;

public class StatusBoletoAVencer extends Status{

	public StatusBoletoAVencer(Status proximo) {
		super(proximo);
	}

	@Override
	public StatusBoleto validar(Payment boleto) {
		if(LocalDate.now().plusDays(7).isAfter(boleto.getDueDate())) {
			return StatusBoleto.A_VENCER;
		}
		return proximo.validar(boleto);
	}

}
