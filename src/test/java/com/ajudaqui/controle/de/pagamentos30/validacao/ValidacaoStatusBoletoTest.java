package com.ajudaqui.controle.de.pagamentos30.validacao;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.time.LocalDate;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import com.ajudaqui.controle.de.pagamentos30.entity.StatusBoleto;


class ValidacaoStatusBoletoTest {

	@Test
	public void deveRetornarStatusVencendoHoje() {
		LocalDate hoje= LocalDate.now();
		StatusBoleto status = ValidacaoStatusBoleto.validacao(hoje);
		
		Assert.assertEquals(StatusBoleto.VENCENDO_HOJE, status);
		assertNotNull(status);
		assertFalse(status.equals(StatusBoleto.A_VENCER));
	}
	@Test
	public void deveRetornarStatusVencido() {
		
		LocalDate vencimento= LocalDate.of(2020, 1, 1);
		StatusBoleto status = ValidacaoStatusBoleto.validacao(vencimento);
		
		Assert.assertEquals(StatusBoleto.VENCIDO, status);
		assertNotNull(status);
		assertFalse(status.equals(StatusBoleto.A_VENCER));
	}
	@Test
	public void deveRetornarStatusA_VENCER() {
		
		LocalDate vencimento= LocalDate.of(2220, 1, 1);
		StatusBoleto status = ValidacaoStatusBoleto.validacao(vencimento);
		
		Assert.assertEquals(StatusBoleto.A_VENCER, status);
		assertNotNull(status);
		assertFalse(status.equals(StatusBoleto.VENCENDO_HOJE));
	}

}
