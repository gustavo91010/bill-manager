package com.ajudaqui.controle.de.pagamentos30.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.ajudaqui.controle.de.pagamentos30.entity.Boleto;
import com.ajudaqui.controle.de.pagamentos30.repository.BoletoRepository;
import com.ajudaqui.controle.de.pagamentos30.service.ValidarStatus;

public class BoletoDto {
	private String descricao;
	private BigDecimal valor;
	private String vencimento;
	
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
	
	public String getVencimento() {
		return vencimento;
	}
	public void setVencimento(String vencimento) {
		this.vencimento = vencimento;
	}
	public Boleto toDatabase(BoletoRepository boletoRepository) {
		Boleto boleto= new Boleto();
		
		boleto.setDescricao(this.descricao);
		boleto.setValor(this.valor);
		DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyy");
		boleto.setVencimento(LocalDate.parse(this.vencimento, formato));
		ValidarStatus.statusAtualizado(boleto, boletoRepository);
		return boleto;
	}
	
	

}
