package com.ajudaqui.billmanager.service;

import static com.ajudaqui.billmanager.utils.StatusBoleto.valueOf;
import static com.ajudaqui.billmanager.utils.ValidarStatus.statusAtualizado;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.ajudaqui.billmanager.controller.from.BoletoFrom;
import com.ajudaqui.billmanager.entity.Payment;
import com.ajudaqui.billmanager.entity.Users;
import com.ajudaqui.billmanager.exception.MsgException;
import com.ajudaqui.billmanager.repository.PaymentsRepository;
import com.ajudaqui.billmanager.service.vo.PayamentDto;
import com.ajudaqui.billmanager.service.vo.Sumary;
import com.ajudaqui.billmanager.utils.StatusBoleto;
import com.ajudaqui.billmanager.utils.ValidarStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

  @Autowired
  private PaymentsRepository paymentRepository;
  @Autowired
  private UsersService usersService;

  private Logger logger = LoggerFactory.getLogger(PaymentService.class.getSimpleName());

  public Payment register(PayamentDto paymentDto, String accessToken) {
    Users users = usersService.findByAccessToken(accessToken);
    Payment payment = paymentDto.toDatabase(users);

    if (isRegistery(payment))
      throw new MsgException("pagamento já cadastrado");
    return save(payment);
  }

  private Payment save(Payment payment) {
    return paymentRepository.save(payment);
  }

  public Payment update(Payment payment) {
    return save(payment);
  }

  public List<Payment> boletosRecorrentes(PayamentDto paymentDto, Long repeticao, String accessToken) {
    List<Payment> registeredPayments = new ArrayList<>();
    int index = 0;
    Users users = usersService.findByAccessToken(accessToken);
    while (index < repeticao) {
      Payment newPayment = paymentDto.toDatabase(users);
      newPayment.setDue_date(newPayment.getDue_date().plusMonths(index));

      if (isRegistery(newPayment)) {
        logger.warn(String.format("Boleto descição: %s, valor: %s, Vencimento: %$ já registrado.",
            newPayment.getDescription(), newPayment.getValue().toString(), newPayment.getDue_date().toString()));
        continue;
      }

      registeredPayments.add(save(newPayment));
      index++;
    }
    return registeredPayments;
  }

  private boolean isRegistery(Payment payment) {
    List<Payment> paymentForMonth = findAllMonth(payment.getUser().getAccessToken(),
        payment.getDue_date().getMonthValue(),
        payment.getDue_date().getYear());

    if (paymentForMonth.isEmpty()) {

      return false;
    }

    return paymentForMonth.contains(payment);
  }

  public Payment findByIdForUsers(String accessToken, Long paymentId) {

    Users users = usersService.findByAccessToken(accessToken);
    Payment boleto = paymentRepository.findByIdForUsers(users.getAccessToken(), paymentId)
        .orElseThrow(() -> new RuntimeException("Boleto não encontrado."));
    return boleto;
  }

  public Payment findById(Long paymentId) {

    Payment boleto = paymentRepository.findById(paymentId)
        .orElseThrow(() -> new RuntimeException("Boleto não encontrado."));
    return boleto;
  }

  public List<Payment> findByPayamentsForUser(String accessToken) {
    return paymentRepository.findByPaymentsForUserAccessToken(accessToken);
  }

  public List<Payment> findPaymentsWeek(String accessToken, String date, String status) {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate dayWeek = LocalDate.parse(date, formatter);
    LocalDate monday = dayWeek.minusDays(dayWeek.getDayOfWeek().getValue() - 1);
    LocalDate sunday = monday.plusDays(6);

    List<Payment> payments = new ArrayList<Payment>();

    if (status.isEmpty()) {
      payments = paymentRepository.findPayaments(accessToken, monday, sunday);

    } else {
      payments = paymentRepository.findPayaments(accessToken, monday, sunday,
          valueOf(status));
    }
    return payments;

  }

  public List<Payment> findAllMonth(String accessToken, Integer month, Integer year) {

    LocalDate startMonth = LocalDate.of(year, month, 1);
    LocalDate endMonth = LocalDate.of(year, month, startMonth.lengthOfMonth());
    List<Payment> resultt = paymentRepository.findPayaments(accessToken, startMonth, endMonth);
    return resultt;

  }

  public Payment update(String accessToken, Long paymentId, BoletoFrom from) {

    Payment payment = findByIdForUsers(accessToken, paymentId);

    if (!from.getDescription().isEmpty()) {
      payment.setDescription(from.getDescription());
    }
    if (from.getValue() != null) {
      payment.setValue(from.getValue());
    }
    if (from.getDue_date() != null) {
      payment.setDue_date(from.getDue_date());
    }
    payment.setUpdated_at(LocalDateTime.now());
    return paymentRepository.save(statusAtualizado(payment));
  }

  // atualização do estado em execução
  public void performStatusUpdate() {
    List<Payment> pagamentos = paymentRepository.nextPayments(LocalDate.now().plusDays(10));
    atualizarStatus(pagamentos);
  }

  public List<Payment> atualizarStatus(List<Payment> boletos) {
    boletos.forEach(ValidarStatus::statusAtualizado);
    return boletos;
  }

  public void deleteById(String accessToken, Long id) {
    paymentRepository.delete(findById(id));
  }

  public List<Payment> periodTime(String accessToken, String description, LocalDate start, LocalDate finish,
      String status) {
    Users user = usersService.findByAccessToken(accessToken);
    start = (start == null) ? LocalDate.now() : start;
    finish = (finish == null) ? LocalDate.now() : finish;
    boolean hasDescription = !description.isEmpty();
    boolean hasStatus = !status.isEmpty();

    List<Payment> response = new ArrayList<>();

    if (hasDescription && hasStatus) {
      response = paymentRepository.findPayaments(user.getAccessToken(), description, start, finish,
          StatusBoleto.valueOf(status));
    } else if (hasDescription) {
      response = paymentRepository.findPayaments(user.getAccessToken(), description, start, finish);
    } else if (hasStatus) {
      response = paymentRepository.findPayaments(user.getAccessToken(), start, finish, StatusBoleto.valueOf(status));
    } else {
      response = paymentRepository.findPayaments(user.getAccessToken(), start, finish);
    }

    response.sort(Comparator.comparing(Payment::getDue_date));
    return response;
  }

  public Sumary sumary(String accessToken, LocalDate start, LocalDate finsh) {
    List<Payment> payments = periodTime(accessToken, "", start, finsh, "");
    BigDecimal totalDue = payments.stream()
        .map(Payment::getValue)
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal amountPaid = payments.stream()
        .filter(payment -> payment.getStatus().equals(StatusBoleto.PAGO))
        .map(Payment::getValue)
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    return new Sumary(totalDue, amountPaid);
  }

  public Payment confirmPayment(String accessToken, Long paymentId) {
    Payment payment = findByIdForUsers(accessToken, paymentId);

    payment.setStatus(StatusBoleto.PAGO);
    payment.setUpdated_at(LocalDateTime.now());
    return paymentRepository.save(payment);
  }

}
