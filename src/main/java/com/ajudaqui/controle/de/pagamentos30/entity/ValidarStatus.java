package com.ajudaqui.controle.de.pagamentos30.entity;

import java.time.LocalDate;

import com.ajudaqui.controle.de.pagamentos30.repository.BoletoRepository;

public class ValidarStatus {

//	public static StatusBoleto statusAtualizado(StatusBoleto status, LocalDate vencimento) {
	public static Boleto statusAtualizado(Boleto boleto, BoletoRepository repository) {
//		A_VENCER, VENCIDO, VENCENDO_HOJE
		LocalDate hoje = LocalDate.now();
		boolean a_vencer = boleto.getVencimento().compareTo(hoje) > 0;
		boolean vencido = boleto.getVencimento().compareTo(hoje) < 0;
		boolean vencendo_hoje = boleto.getVencimento().compareTo(hoje) == 0;
		if (boleto.getStatus() != StatusBoleto.PAGO) {

			if (a_vencer) {
				boleto.setStatus(StatusBoleto.A_VENCER);

			}
			if (vencido) {
				boleto.setStatus(StatusBoleto.VENCIDO);
			}
			if (vencendo_hoje)
				boleto.setStatus(StatusBoleto.VENCENDO_HOJE);
		}
		return repository.save(boleto);
	}

}
