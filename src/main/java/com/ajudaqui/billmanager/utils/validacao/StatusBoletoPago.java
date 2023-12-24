package com.ajudaqui.billmanager.utils.validacao;

import com.ajudaqui.billmanager.entity.Payment;
import com.ajudaqui.billmanager.utils.StatusBoleto;

public class StatusBoletoPago extends Status {

	public StatusBoletoPago(Status proximo) {
		super(proximo);
	}

	@Override
	public StatusBoleto validar(Payment boleto) {
		if(boleto.getStatus()== StatusBoleto.PAGO) {
			return StatusBoleto.PAGO;
		}

		return proximo.validar(boleto);
	}


}
