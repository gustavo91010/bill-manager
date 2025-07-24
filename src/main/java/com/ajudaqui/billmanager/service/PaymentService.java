package com.ajudaqui.billmanager.service;
import com.ajudaqui.billmanager.service.filter.payments.*;

import static com.ajudaqui.billmanager.utils.StatusBoleto.PAGO;
import static com.ajudaqui.billmanager.utils.StatusBoleto.valueOf;
import static com.ajudaqui.billmanager.utils.ValidarStatus.statusAtualizado;
import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.HALF_UP;
import static java.time.LocalDate.of;
import static java.time.LocalDateTime.now;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;

import java.math.BigDecimal;
import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.ajudaqui.billmanager.controller.from.BoletoFrom;
import com.ajudaqui.billmanager.entity.Category;
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
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

  private PaymentsRepository paymentRepository;
  private UsersService usersService;
  private CategoryService categoryService;

  public PaymentService(PaymentsRepository paymentRepository, UsersService usersService,
      CategoryService categoryService) {
    this.paymentRepository = paymentRepository;
    this.usersService = usersService;
    this.categoryService = categoryService;
  }

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
    Category category = null;
    if (paymentDto.getCategory() != null)
      category = categoryService.findByNameOrRegister(paymentDto.getCategory(), users);

    while (index < repeticao) {
      Payment newPayment = paymentDto.toDatabase(users);

      if (category != null)
        newPayment.setCategory(category);

      newPayment.setDueDate(newPayment.getDueDate().plusMonths(index));

      index++;
      if (isRegistery(newPayment)) {
        logger.warn("Boleto descrição: {}, valor: {}, Vencimento: {} já registrado.",
            newPayment.getDescription(),
            newPayment.getValue(),
            newPayment.getDueDate());
        continue;
      }
      registeredPayments.add(save(newPayment));
    }
    return registeredPayments;
  }

  protected boolean isRegistery(Payment payment) {

    List<Payment> paymentForMonth = findAllMonth(payment.getUser().getAccessToken(),
        payment.getDueDate().getMonthValue(),
        payment.getDueDate().getYear());
    if (paymentForMonth.isEmpty())
      return false;
    return paymentForMonth.contains(payment);
  }

  public Payment findByIdForUsers(String accessToken, Long paymentId) {
    return paymentRepository.findByIdForUsers(accessToken, paymentId)
        .orElseThrow(() -> new MsgException("Boleto não encontrado."));
  }

  public Payment findById(Long paymentId) {
    return paymentRepository.findById(paymentId)
        .orElseThrow(() -> new MsgException("Boleto não encontrado."));
  }

  public List<Payment> findByPayamentsForUser(String accessToken) {
    return paymentRepository.findByPaymentsForUserAccessToken(accessToken);
  }

  public List<Payment> findPaymentsWeek(String accessToken, String date, String status) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate dayWeek = LocalDate.parse(date, formatter);
    LocalDate monday = dayWeek.minusDays(dayWeek.minusDays(1).getDayOfWeek().getValue());
    LocalDate sunday = monday.plusDays(6);

    if (status.isEmpty())
      return paymentRepository.findPayaments(accessToken, monday, sunday);

    return paymentRepository.findPayaments(accessToken, monday, sunday,
        valueOf(status));
  }

  public List<Payment> findAllMonth(String accessToken, Integer month, Integer year) {
    LocalDate startMonth = of(year, month, 1);
    LocalDate endMonth = of(year, month, startMonth.lengthOfMonth());
    return paymentRepository.findPayaments(accessToken, startMonth, endMonth);
  }

  public Payment update(String accessToken, Long paymentId, BoletoFrom from) {
    Payment payment = findByIdForUsers(accessToken, paymentId);
    if (!from.getDescription().isEmpty())
      payment.setDescription(from.getDescription());
    if (from.getValue() != null)
      payment.setValue(from.getValue());
    if (from.getDueDate() != null)
      payment.setDueDate(from.getDueDate());
    if (from.getCategory() != null && !from.getCategory().isEmpty()) {
      Category category = categoryService.findByNameOrRegister(from.getCategory(), payment.getUser());
      payment.setCategory(category);
    }
    payment.setUpdatedAt(now());
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
    Payment payment = findById(id);
    if (!accessToken.equals(payment.getUser().getAccessToken()))
      throw new MsgException("Solicitação não autorizada");
    paymentRepository.delete(findById(id));
  }

  // Melhorar esse um dia!
  public List<Payment> periodTime(String accessToken, String description, LocalDate start, LocalDate finish,
      String status) {
    Users user = usersService.findByAccessToken(accessToken);
    start = (start == null) ? LocalDate.now() : start;
    finish = (finish == null) ? LocalDate.now() : finish;
    boolean hasDescription = !description.isEmpty();
    boolean hasStatus = !status.isEmpty();

    return PaymentSearcheStrategy.search(paymentRepository, user.getAccessToken(), description, start, finish,
        status);

    // List<Payment> response;

    // if (hasDescription && hasStatus) {
    // response = paymentRepository.findPayaments(user.getAccessToken(),
    // description, start, finish,
    // StatusBoleto.valueOf(status));
    // } else if (hasDescription) {
    // response = paymentRepository.findPayaments(user.getAccessToken(),
    // description, start, finish);
    // } else if (hasStatus) {
    // response = paymentRepository.findPayaments(user.getAccessToken(), start,
    // finish, StatusBoleto.valueOf(status));
    // } else {
    // response = paymentRepository.findPayaments(user.getAccessToken(), start,
    // finish);
    // }
    // response.sort(Comparator.comparing(Payment::getDueDate));
    // return response;
  }

  public Sumary sumary(String accessToken, LocalDate start, LocalDate finsh) {
    List<Payment> payments = periodTime(accessToken, "", start, finsh, "");
    BigDecimal totalDue = payments.stream()
        .map(Payment::getValue)
        .reduce(ZERO, BigDecimal::add);

    BigDecimal amountPaid = payments.stream()
        .filter(payment -> payment.getStatus().equals(PAGO))
        .map(Payment::getValue)
        .reduce(ZERO, BigDecimal::add);
    return new Sumary(totalDue, amountPaid);
  }

  public Payment confirmPayment(String accessToken, Long paymentId) {
    Payment payment = findByIdForUsers(accessToken, paymentId);
    payment.setStatus(PAGO);
    payment.setUpdatedAt(now());
    return paymentRepository.save(payment);
  }

  public Map<String, BigDecimal> sumaryCategory(String accessToken, LocalDate start, LocalDate finsh) {
    List<Payment> payments = periodTime(accessToken, "", start, finsh, "");
    Map<String, BigDecimal> sumary = payments.stream()
        .collect(groupingBy(
            p -> nameCategoryFactor(p.getCategory()),
            mapping(Payment::getValue, Collectors.reducing(ZERO, BigDecimal::add))));
    sumary.replaceAll((k, v) -> v.setScale(2, HALF_UP));
    return sumary;
  }

  private String nameCategoryFactor(Category category) {
    return category != null ? category.getName() : "sem-categoria";
  }

  public Payment addCategory(String accessToken, Long paymentId, String name) {
    Users users = usersService.findByAccessToken(accessToken);

    Category category = categoryService.findByNameOrRegister(name, users);
    Payment payment = findById(paymentId);
    category.getPayments().add(payment);
    payment.setCategory(category);

    categoryService.update(category);
    return payment;
  }
}
