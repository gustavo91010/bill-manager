package com.ajudaqui.controle.de.pagamentos30.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ajudaqui.controle.de.pagamentos30.validacao.ValidacaoStatusBoleto;

@Entity
@Table(name = "boleto")
public class Boleto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
//	@Column(nullable = false)
	private String descricao;
//	@Column(nullable = false)
	private BigDecimal valor;
//	@Column(nullable = false)
	private LocalDate vencimento;

	@Enumerated(EnumType.STRING)
//	@Column(nullable =true)
//	private StatusBoleto status= ValidacaoStatusBoleto.validacao(this.vencimento);
//	private StatusBoleto status = StatusBoleto.NAO_PAGO;
	private StatusBoleto status ;

	public Boleto() {
		// TODO Auto-generated constructor stub
	}

	public Boleto(Long id, String descricao, BigDecimal valor, LocalDate vencimento,StatusBoleto status) {
		this.id = id;
		this.descricao = descricao;
		this.valor = valor;
		this.vencimento = vencimento;
		this.status = status;
	}
	public Boleto(Long id, String descricao, BigDecimal valor, LocalDate vencimento) {
		this.id = id;
		this.descricao = descricao;
		this.valor = valor;
		this.vencimento = vencimento;
		this.status = ValidacaoStatusBoleto.validacao(vencimento);
	}

	public Boleto( String descricao, BigDecimal valor, LocalDate vencimento) {
		this.descricao = descricao;
		this.valor = valor;
		this.vencimento = vencimento;
		this.status = ValidacaoStatusBoleto.validacao(vencimento);
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
		return  status;
	}

	public void setStatus(StatusBoleto status) {
		this.status = status;

	}

	

	@Override
	public int hashCode() {
		return Objects.hash(descricao, id, status, valor, vencimento);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Boleto other = (Boleto) obj;
		return Objects.equals(descricao, other.descricao) && Objects.equals(id, other.id) && status == other.status
				&& Objects.equals(valor, other.valor) && Objects.equals(vencimento, other.vencimento);
	}


}
