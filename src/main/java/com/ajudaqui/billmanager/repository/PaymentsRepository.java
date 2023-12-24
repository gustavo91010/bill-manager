package com.ajudaqui.billmanager.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.ajudaqui.billmanager.entity.Payment;

public interface PaymentsRepository extends JpaRepository<Payment, Long>,JpaSpecificationExecutor<Payment> {

//	List<Payament> findByDescricao(String descricao);

	@Query(value="SELECT * FROM payment WHERE status='PAGO' and due_date  BETWEEN :inicio AND :fim ", nativeQuery = true)
	List<Payment> findpayamentsPagos(LocalDate inicio, LocalDate fim);
	
	@Query(value="SELECT * FROM payment WHERE status='VENCIDO'", nativeQuery = true)
	List<Payment> findpayamentsVencidos();
	
	@Query("SELECT b FROM Payment b WHERE b.users.id= :userId AND b.due_date  >= :inicioMes AND b.due_date  <= :finalMes")
	List<Payment> findPayamentsInMonth(Long userId, LocalDate inicioMes, LocalDate finalMes );
	
	// 	@Query("SELECT b FROM Payment b WHERE b.status !='PAGO' AND b.due_date  >= :inicioMes AND b.due_date  <= :finalMes")
	@Query("SELECT b FROM Payment b WHERE b.users.id= :userId AND b.status != :status AND b.due_date  >= :inicioMes AND b.due_date  <= :finalMes")
	List<Payment> findForPaymentsByMonthAndStatus(Long userId,LocalDate inicioMes, LocalDate finalMes, String status );
	
	@Query(value="select * from payment where status <> 'PAGO' AND due_date  > :deadline ", nativeQuery = true)
	List<Payment> nextPayments(LocalDate deadline);
	
	@Query(value="select * from payment where users_id= :userId ", nativeQuery = true)
	List<Payment> findByPayamentsForUser(Long userId);

	
	@Query("SELECT b FROM Payment b WHERE b.users.id= :userId AND b.due_date  >= :startMonth AND b.due_date  <= :endMonth")
	List<Payment> findAllMonth(Long userId, LocalDate startMonth, LocalDate endMonth);

	@Query("SELECT b FROM Payment b WHERE b.users.id= :userId AND b.id= :paymentId ")
	Optional<Payment> findByIdForUsers(Long userId, Long paymentId);

	@Query("SELECT b FROM Payment b WHERE b.users.id= :userId AND b.description= :description ")
	List<Payment> findByDescriptionForUsers(Long userId, String description);
}
