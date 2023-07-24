package com.ajudaqui.bill.manager.validacao;

import java.time.LocalDate;

import com.ajudaqui.bill.manager.entity.Payament;
import com.ajudaqui.bill.manager.entity.StatusBoleto;

public class StatusBoletoAVencer extends Status{

	public StatusBoletoAVencer(Status proximo) {
		super(proximo);
		// TODO Auto-generated constructor stub
	}

	@Override
	public StatusBoleto validar(Payament boleto) {
		if(LocalDate.now().plusDays(7).isAfter(boleto.getDue_date())) {
			return StatusBoleto.A_VENCER;
		}
		return proximo.validar(boleto);
	}

}
