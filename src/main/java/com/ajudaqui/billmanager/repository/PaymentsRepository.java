package com.ajudaqui.billmanager.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.ajudaqui.billmanager.entity.Payment;
import com.ajudaqui.billmanager.utils.StatusBoleto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PaymentsRepository extends JpaRepository<Payment, Long> {

  @Query("SELECT b FROM Payment b WHERE b.userId= :userId AND b.due_date  >= :inicioMes AND b.due_date  <= :finalMes")
  List<Payment> findPayaments(Long userId, LocalDate inicioMes, LocalDate finalMes);

  @Query("SELECT b FROM Payment b WHERE b.userId= :userId AND b.description= :description AND b.due_date  >= :inicioMes AND b.due_date  <= :finalMes")
  List<Payment> findPayaments(Long userId, String description, LocalDate inicioMes, LocalDate finalMes);

  @Query("SELECT b FROM Payment b WHERE b.userId = :userId AND b.status = :status AND b.due_date >= :inicioMes AND b.due_date <= :finalMes")
  List<Payment> findPayaments(Long userId, LocalDate inicioMes, LocalDate finalMes, StatusBoleto status);

  @Query("SELECT b FROM Payment b WHERE b.userId = :userId AND b.description= :description AND b.status = :status AND b.due_date >= :inicioMes AND b.due_date <= :finalMes")
  List<Payment> findPayaments(Long userId, String description, LocalDate inicioMes, LocalDate finalMes,
      StatusBoleto status);

  @Query(value = "select * from payment where status <> 'PAGO' AND due_date  > :deadline ", nativeQuery = true)
  List<Payment> nextPayments(LocalDate deadline);

  @Query(value = "select * from payment where users_id= :userId ", nativeQuery = true)
  List<Payment> findByPayamentsForUser(Long userId);

  @Query("SELECT b FROM Payment b WHERE  b.userId= :userId AND b.id= :paymentId ")
  Optional<Payment> findByIdForUsers(Long userId, Long paymentId);

}
