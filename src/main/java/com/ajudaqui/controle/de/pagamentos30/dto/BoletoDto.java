package com.ajudaqui.controle.de.pagamentos30.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.ajudaqui.controle.de.pagamentos30.entity.Boleto;
import com.ajudaqui.controle.de.pagamentos30.entity.StatusBoleto;

public class BoletoDto {
	private String descricao;
	private BigDecimal valor;
	private LocalDate vencimento;
	
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
	
	public Boleto toDatabase() {
		Boleto boleto= new Boleto();
		
		boleto.setDescricao(this.descricao);
		boleto.setValor(this.valor);
		boleto.setVencimento(this.vencimento);
		boleto.setStatus(StatusBoleto.NAO_PAGO);
		
		return boleto;
	}
	
	

}
