package com.ajudaqui.bill.manager.validacao;

import com.ajudaqui.bill.manager.entity.Boleto;
import com.ajudaqui.bill.manager.entity.StatusBoleto;

public abstract class Status {
	protected Status proximo;

	public Status(Status proximo) {
		super();
		this.proximo = proximo;
	}
	
	public abstract StatusBoleto validar(Boleto boleto);
	
	

}