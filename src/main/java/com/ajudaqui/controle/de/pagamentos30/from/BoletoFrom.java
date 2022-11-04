package com.ajudaqui.controle.de.pagamentos30.from;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.ajudaqui.controle.de.pagamentos30.entity.Boleto;
import com.ajudaqui.controle.de.pagamentos30.entity.StatusBoleto;
import com.ajudaqui.controle.de.pagamentos30.validacao.ValidacaoStatusBoleto;

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

	public Boleto convert() {
		Boleto boleto= new Boleto();
		boleto.setDescricao(this.descricao);
		boleto.setValor(this.valor);
		boleto.setVencimento(this.vencimento);
		boleto.setStatus(ValidacaoStatusBoleto.validacao(this.vencimento));

		return boleto;
	}

}
