package com.ajudaqui.controle.de.pagamentos30.entity.Vo;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.ajudaqui.controle.de.pagamentos30.entity.Boleto;
import com.ajudaqui.controle.de.pagamentos30.entity.StatusBoleto;
import com.ajudaqui.controle.de.pagamentos30.validacao.ValidacaoStatusBoleto;

public class BoletoVO {

	private Long id;
	private String descricao;
	private BigDecimal valor;
	private LocalDate vencimento;
	private StatusBoleto status;

	public BoletoVO(Boleto boleto) {
		this.id = boleto.getId();
		this.descricao = boleto.getDescricao();
		this.valor = boleto.getValor();
		this.vencimento = boleto.getVencimento();
		this.status = ValidacaoStatusBoleto.validacao(boleto.getVencimento());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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
		return status;
	}

	public void setStatus(StatusBoleto status) {
		this.status = status;
	}

	

}
