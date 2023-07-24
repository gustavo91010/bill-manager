package com.ajudaqui.bill.manager.validacao;

import com.ajudaqui.bill.manager.entity.Boleto;
import com.ajudaqui.bill.manager.entity.StatusBoleto;

public class BoletosEmDias extends Status {
	public BoletosEmDias() {
		super(null);
	}

	@Override
	public StatusBoleto validar(Boleto boleto) {
		return StatusBoleto.EM_DIAS;
	}

}
