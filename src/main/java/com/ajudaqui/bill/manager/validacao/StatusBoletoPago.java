package com.ajudaqui.bill.manager.validacao;

import com.ajudaqui.bill.manager.entity.Payament;
import com.ajudaqui.bill.manager.entity.StatusBoleto;

public class StatusBoletoPago extends Status {

	public StatusBoletoPago(Status proximo) {
		super(proximo);
	}

	@Override
	public StatusBoleto validar(Payament boleto) {
		if(boleto.getStatus()== StatusBoleto.PAGO) {
			return StatusBoleto.PAGO;
		}

		return proximo.validar(boleto);
	}


}
