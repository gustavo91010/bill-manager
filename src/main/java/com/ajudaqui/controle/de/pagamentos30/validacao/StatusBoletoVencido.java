package com.ajudaqui.controle.de.pagamentos30.validacao;

import java.time.LocalDate;

import com.ajudaqui.controle.de.pagamentos30.entity.Boleto;
import com.ajudaqui.controle.de.pagamentos30.entity.StatusBoleto;

public class StatusBoletoVencido extends Status {
	private LocalDate hoje = LocalDate.now();

	public StatusBoletoVencido(Status proximo) {
		super(proximo);
	}

	@Override
	public StatusBoleto validar(Boleto boleto) {
		boolean vencido = boleto.getVencimento().compareTo(hoje) < 0;
		if (vencido) {
			return StatusBoleto.VENCIDO;
		}
		return proximo.validar(boleto);
	}

}
