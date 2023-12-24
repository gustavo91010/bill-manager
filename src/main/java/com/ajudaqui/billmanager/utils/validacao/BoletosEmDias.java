package com.ajudaqui.billmanager.utils.validacao;

import com.ajudaqui.billmanager.entity.Payment;
import com.ajudaqui.billmanager.utils.StatusBoleto;

public class BoletosEmDias extends Status {
	public BoletosEmDias() {
		super(null);
	}

	@Override
	public StatusBoleto validar(Payment boleto) {
		return StatusBoleto.EM_DIAS;
	}

}
