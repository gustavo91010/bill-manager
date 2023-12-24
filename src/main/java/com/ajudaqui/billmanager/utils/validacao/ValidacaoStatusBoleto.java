package com.ajudaqui.billmanager.utils.validacao;

import java.time.LocalDate;

import com.ajudaqui.billmanager.utils.StatusBoleto;

public class ValidacaoStatusBoleto {

	public static StatusBoleto validacao(LocalDate vencimento) {
		int verificacao = vencimento.compareTo(LocalDate.now());
		
		if (verificacao == 0) {
			return StatusBoleto.VENCENDO_HOJE;
		} else if (verificacao < 0) {
			return StatusBoleto.VENCIDO;
		}
		return StatusBoleto.A_VENCER;

	}
//	public static StatusBoleto vencimentoProximo(LocalDate vencimento) {
//		LocalDate hojeAOito=LocalDate.of(vencimento.getYear(), vencimento.getMonth(), vencimento.getDayOfMonth()+7);
//boolean validacao= hojeAOito >= LocalDate.now();
//		return null;
//	}

}
