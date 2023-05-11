package com.ajudaqui.controle.de.pagamentos30.specification;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import com.ajudaqui.controle.de.pagamentos30.entity.Boleto;
import com.ajudaqui.controle.de.pagamentos30.entity.StatusBoleto;

public class BoletoSpecification {
	public static Specification<Boleto> descricao(String descricao){
		return (root, criteriaQuery, criteriaBuilder) ->
		criteriaBuilder.like(root.get("descricao"), "%"+descricao+"%");
	}
	
	public static Specification<Boleto> status(StatusBoleto status){
		return (root, criteriaQuery, criteriaBuilder) ->
		criteriaBuilder.equal(root.get("status"), "%"+status+"%");
	};
	
	public static Specification<Boleto> vencimento(LocalDate vencimento){
		return (root, criteriaQuery, criteriaBuilder) ->
		criteriaBuilder.greaterThan(root.get("vencimento"), "%"+vencimento+"%");
	};
	

}
