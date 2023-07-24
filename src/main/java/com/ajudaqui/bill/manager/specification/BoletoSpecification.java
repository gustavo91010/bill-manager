package com.ajudaqui.bill.manager.specification;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import com.ajudaqui.bill.manager.entity.Payament;
import com.ajudaqui.bill.manager.entity.StatusBoleto;

public class BoletoSpecification {
	public static Specification<Payament> descricao(String descricao){
		return (root, criteriaQuery, criteriaBuilder) ->
		criteriaBuilder.like(root.get("descricao"), "%"+descricao+"%");
	}
	
	public static Specification<Payament> status(StatusBoleto status){
		return (root, criteriaQuery, criteriaBuilder) ->
		criteriaBuilder.equal(root.get("status"), "%"+status+"%");
	};
	
	public static Specification<Payament> vencimento(LocalDate vencimento){
		return (root, criteriaQuery, criteriaBuilder) ->
		criteriaBuilder.greaterThan(root.get("vencimento"), "%"+vencimento+"%");
	};
	

}
