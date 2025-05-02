package com.ajudaqui.billmanager.service;

import static com.ajudaqui.billmanager.utils.StatusBoleto.valueOf;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ajudaqui.billmanager.controller.from.BoletoFrom;
import com.ajudaqui.billmanager.entity.Payment;
import com.ajudaqui.billmanager.entity.Users;
import com.ajudaqui.billmanager.exception.MsgException;
import com.ajudaqui.billmanager.repository.PaymentsRepository;
import com.ajudaqui.billmanager.service.vo.PayamentDto;
import com.ajudaqui.billmanager.utils.StatusBoleto;
import com.ajudaqui.billmanager.utils.ValidarStatus;

@Service
public class PaymentService {

  @Autowired
  private PaymentsRepository paymentRepository;
  @Autowired
  private UsersService usersService;

  private Logger logger = LoggerFactory.getLogger(PaymentService.class.getSimpleName());

  public Payment register(PayamentDto paymentDto, String accessToken) {
    // ta criando reétido... testar isso...
    Users users = usersService.findByAccessToken(accessToken);
    Payment payment = paymentDto.toDatabase(users.getId());

    if (isRegistery(payment)) {
      throw new MsgException("pagamento já cadastrado");
    }
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
      Payment newPayment = paymentDto.toDatabase(users.getId());
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
    List<Payment> paymentForMonth = findAllMonth(payment.getUserId(), payment.getDue_date().getMonthValue(),
        payment.getDue_date().getYear());

    if (paymentForMonth.isEmpty()) {

      return false;
    }

    return paymentForMonth.contains(payment);
  }

  // public List<Payment> searchLatePayments(String accessToken) {
  // Users users = usersService.findByAccessToken(accessToken);
  // return paymentRepository.searchLatePayments(users.getId());

  // }

  public Payment findByIdForUsers(String accessToken, Long paymentId) {

    Users users = usersService.findByAccessToken(accessToken);
    Payment boleto = paymentRepository.findByIdForUsers(users.getId(), paymentId)
        .orElseThrow(() -> new RuntimeException("Boleto não encontrado."));
    return boleto;
  }

  public Payment findById(Long paymentId) {

    Payment boleto = paymentRepository.findById(paymentId)
        .orElseThrow(() -> new RuntimeException("Boleto não encontrado."));
    return boleto;
  }

  public List<Payment> findByPayamentsForUser(Long userId) {

    return paymentRepository.findByPayamentsForUser(userId);
  }

  public List<Payment> findPaymentsWeek(Long usersId, String date, String status) {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate dayWeek = LocalDate.parse(date, formatter);
    LocalDate monday = dayWeek.minusDays(dayWeek.getDayOfWeek().getValue() - 1);
    LocalDate sunday = monday.plusDays(6);

    List<Payment> payments = new ArrayList<Payment>();

    if (status.isEmpty()) {
      payments = paymentRepository.findPayaments(usersId, monday, sunday);

    } else {
      payments = paymentRepository.findPayaments(usersId, monday, sunday,
          valueOf(status));
    }
    return payments;

  }

  public List<Payment> findAllMonth(Long userId, Integer month, Integer year) {

    LocalDate startMonth = LocalDate.of(year, month, 1);
    LocalDate endMonth = LocalDate.of(year, month, startMonth.lengthOfMonth());
    List<Payment> resultt = paymentRepository.findPayaments(userId, startMonth, endMonth);
    return resultt;

  }

  // public List<Payment> searcheByUsersByMonthAndStatus(Long usersId, Integer
  // month, Integer year, String status) {

  // // se o mes tiver vazio, sera o periodo de 12 meses, o ano, sera 1900
  // LocalDate startMonth = LocalDate.of(year == 0 ? 1900 : year, month == 0 ? 1 :
  // month, 1);
  // LocalDate endMonth = LocalDate.of(year == 0 ? 1900 : year, month == 0 ? 12 :
  // month, startMonth.lengthOfMonth());

  // List<Payment> payments = new ArrayList<Payment>();

  // if (status.isEmpty()) {
  // payments = paymentRepository.findPayamentsInMonth(usersId, startMonth,
  // endMonth);
  // } else {

  // payments = paymentRepository.findForPaymentsByMonthAndStatus(usersId,
  // startMonth, endMonth,
  // valueOf(status));
  // }
  // return payments;
  // }

  // public List<Payment> findPaymentDaily(Long usersId) {
  // LocalDate startMonth = LocalDate.now();
  // LocalDate endMonth = startMonth;

  // return paymentRepository.findPayamentsInMonth(usersId, startMonth, endMonth);
  // }

  // public List<Payment> findByDescricao(String accessToken, String descricao) {
  // Users users = usersService.findByAccessToken(accessToken);
  // List<Payment> boletos =
  // paymentRepository.findByDescriptionForUsers(users.getId(), descricao);
  // return boletos;
  // }

  // Efetivar Pagamento
  public Payment makePayment(Long id) {
    Payment boleto = findById(id);

    boleto.setStatus(StatusBoleto.PAGO);
    boleto.setUpdated_at(LocalDateTime.now());

    paymentRepository.save(boleto);
    return boleto;

  }

  public Payment update(String accessToken, Long paymentId, BoletoFrom from) {

    Payment payment = findByIdForUsers(accessToken, paymentId);

    if (!from.getDescricao().isEmpty()) {
      payment.setDescription(from.getDescricao());
    }
    if (from.getValor() != null) {
      payment.setValue(from.getValor());
    }
    if (from.getVencimento() != null) {
      payment.setDue_date(from.getVencimento());
    }
    payment.setUpdated_at(LocalDateTime.now());
    return paymentRepository.save(payment);
  }

  // atualização do estado em execução
  public void performStatusUpdate() {
    List<Payment> pagamentos = paymentRepository.nextPayments(LocalDate.now().plusDays(10));
    atualizarStatus(pagamentos);
  }

  public List<Payment> atualizarStatus(List<Payment> boletos) {
    boletos.forEach(b -> {
      ValidarStatus.statusAtualizado(b);

    });
    return boletos;

  }

  public void deleteById(String accessToken, Long id) {
    Payment boleto = findById(id);
    paymentRepository.delete(boleto);

  }

  public void lalala(String accessToken, String description, LocalDate start, LocalDate finsh,
      String status) {

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
      response = paymentRepository.findPayaments(user.getId(), description, start, finish,
          StatusBoleto.valueOf(status));
    } else if (hasDescription) {
      response = paymentRepository.findPayaments(user.getId(), description, start, finish);
    } else if (hasStatus) {
      response = paymentRepository.findPayaments(user.getId(), start, finish, StatusBoleto.valueOf(status));
    } else {
      response = paymentRepository.findPayaments(user.getId(), start, finish);
    }

    return response;
  }

}
