package com.ajudaqui.billmanager.controller.from;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.ajudaqui.billmanager.entity.Payment;
import com.ajudaqui.billmanager.utils.StatusBoleto;
import com.ajudaqui.billmanager.utils.validacao.ValidacaoStatusBoleto;

public class BoletoFrom {
	private String descricao;
	private BigDecimal valor;
	private LocalDate vencimento;
//	private StatusBoleto status= ValidacaoStatusBoleto.validacao(this.vencimento);

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public LocalDate getVencimento() {
		return vencimento;
	}

	public void setVencimento(LocalDate vencimento) {
		this.vencimento = vencimento;
	}
	

	public StatusBoleto getStatus() {
		return ValidacaoStatusBoleto.validacao(this.vencimento);
	}

//	public void setStatus(StatusBoleto status) {
//		this.status = status;
//	}

	public Payment convert() {
		Payment payament= new Payment();
		payament.setDescription(this.descricao);
		payament.setValue(this.valor);
		payament.setDue_date(this.vencimento);
		payament.setStatus(ValidacaoStatusBoleto.validacao(this.vencimento));

		return payament;
	}

}
