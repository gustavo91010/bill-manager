package com.ajudaqui.controle.de.pagamentos30.validacao;

import com.ajudaqui.controle.de.pagamentos30.entity.Boleto;
import com.ajudaqui.controle.de.pagamentos30.entity.StatusBoleto;

public class BoletosEmDias extends Status {
	public BoletosEmDias() {
		super(null);
	}

	@Override
	public StatusBoleto validar(Boleto boleto) {
		return StatusBoleto.EM_DIAS;
	}

}
