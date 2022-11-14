package com.ajudaqui.controle.de.pagamentos30.validacao;

import com.ajudaqui.controle.de.pagamentos30.entity.Boleto;
import com.ajudaqui.controle.de.pagamentos30.entity.StatusBoleto;

public abstract class Status {
	protected Status proximo;

	public Status(Status proximo) {
		super();
		this.proximo = proximo;
	}
	
	public abstract StatusBoleto validar(Boleto boleto);
	
	

}
