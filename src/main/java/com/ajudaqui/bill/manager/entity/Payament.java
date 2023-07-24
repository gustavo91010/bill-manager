package com.ajudaqui.bill.manager.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ajudaqui.bill.manager.validacao.ValidacaoStatusBoleto;

@Entity
@Table(name = "payament")
public class Payament {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
//	@Column(nullable = false)
	private String description;
//	@Column(nullable = false)
	private BigDecimal value;
//	@Column(nullable = false)
	private LocalDate due_date;
	private LocalDateTime created_at;
	private LocalDateTime updated_at ;

	@Enumerated(EnumType.STRING)
	private StatusBoleto status ;
	

	public Payament() {
		// TODO Auto-generated constructor stub
	}

	public Payament(Long id, String description, BigDecimal value, LocalDate due_date,StatusBoleto status) {
		this.id = id;
		this.description = description;
		this.value = value;
		this.due_date = due_date;
		this.status = status;
		this.created_at= LocalDateTime.now();
	}
	public Payament(Long id, String description, BigDecimal value, LocalDate due_date) {
		this.id = id;
		this.description = description;
		this.value = value;
		this.due_date = due_date;
		this.status = ValidacaoStatusBoleto.validacao(due_date);
		this.created_at= LocalDateTime.now();

	}

	public Payament( String description, BigDecimal value, LocalDate due_date) {
		this.description = description;
		this.value = value;
		this.due_date = due_date;
		this.status = ValidacaoStatusBoleto.validacao(due_date);
		this.created_at= LocalDateTime.now();

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public LocalDate getDue_date() {
		return due_date;
	}

	public void setDue_date(LocalDate due_date) {
		this.due_date = due_date;
	}

	public LocalDateTime getCreated_at() {
		return created_at;
	}

	public void setCreated_at(LocalDateTime created_at) {
		this.created_at = created_at;
	}

	public LocalDateTime getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(LocalDateTime updated_at) {
		this.updated_at = updated_at;
	}

	public StatusBoleto getStatus() {
		return status;
	}

	public void setStatus(StatusBoleto status) {
		this.status = status;
	}

	@Override
	public int hashCode() {
		return Objects.hash(created_at, description, due_date, id, status, updated_at, value);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Payament other = (Payament) obj;
		return Objects.equals(created_at, other.created_at) && Objects.equals(description, other.description)
				&& Objects.equals(due_date, other.due_date) && Objects.equals(id, other.id) && status == other.status
				&& Objects.equals(updated_at, other.updated_at) && Objects.equals(value, other.value);
	}
	
	



}
