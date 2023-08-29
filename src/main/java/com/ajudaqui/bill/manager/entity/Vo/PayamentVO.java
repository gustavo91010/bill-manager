package com.ajudaqui.bill.manager.entity.Vo;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.ajudaqui.bill.manager.entity.Payment;
import com.ajudaqui.bill.manager.entity.StatusBoleto;

public class PayamentVO {

	private Long id;
	private String description;
	private BigDecimal value;
	private LocalDate due_date;
	private StatusBoleto status;
	public PayamentVO() {
		// TODO Auto-generated constructor stub
	}

	public PayamentVO(Payment payaments) {
		this.id = payaments.getId();
		this.description = payaments.getDescription();
		this.value = payaments.getValue();
		this.due_date = payaments.getDue_date();
//		this.status = ValidacaoStatusBoleto.validacao(boleto.getVencimento());
		this.status = payaments.getStatus();
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

	public StatusBoleto getStatus() {
		return status;
	}

	public void setStatus(StatusBoleto status) {
		this.status = status;
	}
	
	
	


	

}
