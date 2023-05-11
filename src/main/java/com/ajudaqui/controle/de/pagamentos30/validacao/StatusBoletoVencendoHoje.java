package com.ajudaqui.controle.de.pagamentos30.validacao;

import java.time.LocalDate;

import com.ajudaqui.controle.de.pagamentos30.entity.Boleto;
import com.ajudaqui.controle.de.pagamentos30.entity.StatusBoleto;

public class StatusBoletoVencendoHoje extends Status {

	private LocalDate hoje= LocalDate.now();
	public StatusBoletoVencendoHoje(Status proximo) {
		super(proximo);
		// TODO Auto-generated constructor stub
	}

	@Override
	public StatusBoleto validar(Boleto boleto) {
		boolean vencendo_hoje = boleto.getVencimento().compareTo(hoje) == 0;
		if(vencendo_hoje) {
			return StatusBoleto.VENCENDO_HOJE;
		}
		return proximo.validar(boleto);
	}
	

}
