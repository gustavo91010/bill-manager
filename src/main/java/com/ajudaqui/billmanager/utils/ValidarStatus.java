package com.ajudaqui.billmanager.utils;

import com.ajudaqui.billmanager.entity.Payment;
import com.ajudaqui.billmanager.repository.PaymentsRepository;
import com.ajudaqui.billmanager.utils.validacao.BoletosEmDias;
import com.ajudaqui.billmanager.utils.validacao.Status;
import com.ajudaqui.billmanager.utils.validacao.StatusBoletoAVencer;
import com.ajudaqui.billmanager.utils.validacao.StatusBoletoPago;
import com.ajudaqui.billmanager.utils.validacao.StatusBoletoVencendoHoje;
import com.ajudaqui.billmanager.utils.validacao.StatusBoletoVencido;

public class ValidarStatus {
	public static Payment statusAtualizado(Payment boleto, PaymentsRepository repository) {
		
		Status status= new StatusBoletoPago(
						   new StatusBoletoVencido(
							   new StatusBoletoVencendoHoje(
								   new StatusBoletoAVencer(
										new BoletosEmDias()))));
		
		boleto.setStatus(status.validar(boleto));
		return repository.save(boleto);
	}

}
