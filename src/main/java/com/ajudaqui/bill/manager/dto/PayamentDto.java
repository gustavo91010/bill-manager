package com.ajudaqui.bill.manager.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.ajudaqui.bill.manager.entity.Payment;
import com.ajudaqui.bill.manager.entity.Users;
import com.ajudaqui.bill.manager.repository.PaymentsRepository;
import com.ajudaqui.bill.manager.service.ValidarStatus;

public class PayamentDto {
	private Long usersId;
	private String descricao;
	private BigDecimal valor;
	private String vencimento;
	
	
	
	public Long getUsersId() {
		return usersId;
	}
	public void setUsersId(Long usersId) {
		this.usersId = usersId;
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
	
	public String getVencimento() {
		return vencimento;
	}
	public void setVencimento(String vencimento) {
		this.vencimento = vencimento;
	}
	public Payment toDatabase(PaymentsRepository boletoRepository, Users users) {
		Payment payament= new Payment();
		
//		payament.setUsers(users);
		payament.setDescription(this.descricao);
		payament.setValue(this.valor);
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		payament.setDue_date(LocalDate.parse(this.vencimento, formato));
		ValidarStatus.statusAtualizado(payament, boletoRepository);
		return payament;
	}
	
	

}
