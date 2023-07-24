package com.ajudaqui.bill.manager.entity.Vo;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.ajudaqui.bill.manager.entity.Boleto;
import com.ajudaqui.bill.manager.entity.StatusBoleto;

public class BoletoVO {

	private Long id;
	private String descricao;
	private BigDecimal valor;
	private LocalDate vencimento;
	private StatusBoleto status;
	public BoletoVO() {
		// TODO Auto-generated constructor stub
	}

	public BoletoVO(Boleto boleto) {
		this.id = boleto.getId();
		this.descricao = boleto.getDescricao();
		this.valor = boleto.getValor();
		this.vencimento = boleto.getVencimento();
//		this.status = ValidacaoStatusBoleto.validacao(boleto.getVencimento());
		this.status = boleto.getStatus();
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
