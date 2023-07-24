package com.ajudaqui.bill.manager.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.ajudaqui.bill.manager.entity.Payament;

public interface PayamentsRepository extends JpaRepository<Payament, Long>,JpaSpecificationExecutor<Payament> {

//	List<Payament> findByDescricao(String descricao);

	@Query(value="SELECT * FROM payament WHERE status='PAGO' and due_date  BETWEEN :inicio AND :fim ", nativeQuery = true)
	List<Payament> findpayamentsPagos(LocalDate inicio, LocalDate fim);
	
	@Query(value="SELECT * FROM payament WHERE status='VENCIDO'", nativeQuery = true)
	List<Payament> findpayamentsVencidos();
	
	@Query("SELECT b FROM Payament b WHERE b.due_date  >= :inicioMes AND b.due_date  <= :finalMes")
	List<Payament> findpayamentsDoMes(LocalDate inicioMes, LocalDate finalMes );
	
	@Query("SELECT b FROM Payament b WHERE b.status !='PAGO' AND b.due_date  >= :inicioMes AND b.due_date  <= :finalMes")
	List<Payament> findpayamentsASeremPagosNoMes(LocalDate inicioMes, LocalDate finalMes );
	
	@Query(value="select * from payament where status <> 'PAGO' AND due_date  > :deadline ", nativeQuery = true)
	List<Payament> nextPayments(LocalDate deadline);
	
;
	

}
