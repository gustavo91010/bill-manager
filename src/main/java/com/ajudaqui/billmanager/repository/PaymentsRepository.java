package com.ajudaqui.billmanager.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.ajudaqui.billmanager.entity.Payment;
import com.ajudaqui.billmanager.utils.StatusBoleto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PaymentsRepository extends JpaRepository<Payment, Long> {

  @Query("SELECT b FROM Payment b WHERE b.user.accessToken = :accessToken AND b.dueDate >= :inicioMes AND b.dueDate <= :finalMes")
  List<Payment> findPayaments(String accessToken, LocalDate inicioMes, LocalDate finalMes);

  @Query("SELECT b FROM Payment b WHERE b.user.accessToken = :accessToken AND b.description = :description AND b.dueDate >= :inicioMes AND b.dueDate <= :finalMes")
  List<Payment> findPayaments(String accessToken, String description, LocalDate inicioMes, LocalDate finalMes);

  @Query("SELECT b FROM Payment b WHERE b.user.accessToken = :accessToken AND b.status = :status AND b.dueDate >= :inicioMes AND b.dueDate <= :finalMes")
  List<Payment> findPayaments(String accessToken, LocalDate inicioMes, LocalDate finalMes, StatusBoleto status);

  @Query("SELECT b FROM Payment b WHERE b.user.accessToken = :accessToken AND b.description = :description AND b.status = :status AND b.dueDate >= :inicioMes AND b.dueDate <= :finalMes")
  List<Payment> findPayaments(String accessToken, String description, LocalDate inicioMes, LocalDate finalMes, StatusBoleto status);

  @Query(value = "select * from payment where status <> 'PAGO' AND dueDate > :deadline ", nativeQuery = true)
  List<Payment> nextPayments(LocalDate deadline);

  @Query(value = "select * from payment where users_id = (select id from users where accessToken = :accessToken)", nativeQuery = true)
  List<Payment> findByPaymentsForUserAccessToken(String accessToken);

  @Query("SELECT b FROM Payment b WHERE b.user.accessToken = :accessToken AND b.id = :paymentId")
  Optional<Payment> findByIdForUsers(String accessToken, Long paymentId);

}
