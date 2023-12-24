package com.ajudaqui.billmanager.service.vo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.ajudaqui.billmanager.entity.Payment;
import com.ajudaqui.billmanager.entity.Users;
import com.ajudaqui.billmanager.repository.PaymentsRepository;
import com.ajudaqui.billmanager.utils.StatusBoleto;
import com.ajudaqui.billmanager.utils.ValidarStatus;

public class PayamentDto {
	private String description;
	private BigDecimal value;
	private LocalDate due_date;
	private StatusBoleto status;

	public PayamentDto() {
	}
	
	public PayamentDto(Payment payaments) {
		this.description = payaments.getDescription();
		this.value = payaments.getValue();
		this.due_date = payaments.getDue_date();
//		this.status = ValidacaoStatusBoleto.validacao(boleto.getVencimento());
		this.status = payaments.getStatus();
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

	public Payment toDatabase(PaymentsRepository boletoRepository, Users users) {
		Payment payament = new Payment();
//		private LocalDateTime created_at;
//		private LocalDateTime updated_at ;
		payament.setUsers(users);
		payament.setDescription(this.description);
		payament.setValue(this.value);
//		DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		payament.setCreated_at(LocalDateTime.now());
		payament.setUpdated_at(LocalDateTime.now());

		
		payament.setDue_date(this.due_date);
		ValidarStatus.statusAtualizado(payament, boletoRepository);
		return payament;
	}

}
