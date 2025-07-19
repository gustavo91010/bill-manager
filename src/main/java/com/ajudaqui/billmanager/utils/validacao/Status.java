package com.ajudaqui.billmanager.utils.validacao;

import com.ajudaqui.billmanager.entity.Payment;
import com.ajudaqui.billmanager.utils.StatusBoleto;

public abstract class Status {
	protected Status proximo;

	protected Status(Status proximo) {
		super();
		this.proximo = proximo;
	}
	
	public abstract StatusBoleto validar(Payment boleto);
	
	

}
