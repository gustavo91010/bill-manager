package com.ajudaqui.controle.de.pagamentos30.validacao;

import com.ajudaqui.controle.de.pagamentos30.entity.Boleto;
import com.ajudaqui.controle.de.pagamentos30.entity.StatusBoleto;

public class StatusBoletoPago extends Status {

	public StatusBoletoPago(Status proximo) {
		super(proximo);
	}

	@Override
	public StatusBoleto validar(Boleto boleto) {
		if(boleto.getStatus()== StatusBoleto.PAGO) {
			return StatusBoleto.PAGO;
		}

		return proximo.validar(boleto);
	}


}
