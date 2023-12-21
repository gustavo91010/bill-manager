package com.ajudaqui.bill.manager.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.ajudaqui.bill.manager.entity.Payment;

public interface PaymentsRepository extends JpaRepository<Payment, Long>,JpaSpecificationExecutor<Payment> {

//	List<Payament> findByDescricao(String descricao);

	@Query(value="SELECT * FROM payment WHERE status='PAGO' and due_date  BETWEEN :inicio AND :fim ", nativeQuery = true)
	List<Payment> findpayamentsPagos(LocalDate inicio, LocalDate fim);
	
	@Query(value="SELECT * FROM payment WHERE status='VENCIDO'", nativeQuery = true)
	List<Payment> findpayamentsVencidos();
	
	@Query("SELECT b FROM Payment b WHERE b.due_date  >= :inicioMes AND b.due_date  <= :finalMes")
	List<Payment> findPayamentsInMonth(LocalDate inicioMes, LocalDate finalMes );
	
	// 	@Query("SELECT b FROM Payment b WHERE b.status !='PAGO' AND b.due_date  >= :inicioMes AND b.due_date  <= :finalMes")
	@Query("SELECT b FROM Payment b WHERE b.status != :status AND b.due_date  >= :inicioMes AND b.due_date  <= :finalMes")
	List<Payment> findForPaymentsByMonthAndStatus(LocalDate inicioMes, LocalDate finalMes, String status );
	
	@Query(value="select * from payment where status <> 'PAGO' AND due_date  > :deadline ", nativeQuery = true)
	List<Payment> nextPayments(LocalDate deadline);
	
	@Query(value="select * from payment where users_id= :userId ", nativeQuery = true)
	List<Payment> findByPayamentsForUser(Long userId);
	
	
;
	

}
