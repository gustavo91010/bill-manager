package com.ajudaqui.bill.manager.validacao;

import com.ajudaqui.bill.manager.entity.Payment;
import com.ajudaqui.bill.manager.entity.StatusBoleto;

public class BoletosEmDias extends Status {
	public BoletosEmDias() {
		super(null);
	}

	@Override
	public StatusBoleto validar(Payment boleto) {
		return StatusBoleto.EM_DIAS;
	}

}
