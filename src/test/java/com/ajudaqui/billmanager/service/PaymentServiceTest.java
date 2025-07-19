package com.ajudaqui.billmanager.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ajudaqui.billmanager.entity.Category;
import com.ajudaqui.billmanager.entity.Payment;
import com.ajudaqui.billmanager.entity.Users;
import com.ajudaqui.billmanager.repository.PaymentsRepository;
import com.ajudaqui.billmanager.service.vo.PayamentDto;
import com.ajudaqui.billmanager.service.vo.Sumary;
import com.ajudaqui.billmanager.utils.StatusBoleto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class PaymentServiceTest {

  @InjectMocks
  private PaymentService payamentService;
  @Mock
  private PaymentsRepository paymentsRepository;
  @Mock
  private UsersService usersService;
  @Mock
  private CategoryService categoryService;

  @Captor
  private ArgumentCaptor<Payment> paymentCaptor;

  @Test
  @DisplayName("Deve regsitrar uma vez com o recorrente")
  public void mustRegisterOnceWithTheApplicant() {

    PayamentDto paymentDto = new PayamentDto();
    paymentDto.setDue_date(LocalDate.now());
    Long repeticao = 1L;

    String accessToken = "";
    Users user = new Users();
    when(usersService.findByAccessToken(accessToken)).thenReturn(user);

    // Execução:
    List<Payment> response = payamentService.boletosRecorrentes(paymentDto, repeticao, accessToken);

    verify(paymentsRepository, times(repeticao.intValue())).save(paymentCaptor.capture());
    List<Payment> capturedPayments = paymentCaptor.getAllValues();

    assertEquals(repeticao, capturedPayments.size());
    assertEquals(paymentDto.getDue_date(), capturedPayments.get(0).getDue_date());
    assertEquals(paymentDto.getDue_date().plusMonths(repeticao - 1),
        capturedPayments.get(capturedPayments.size() - 1).getDue_date());
    assertEquals(repeticao, response.size());
  }

  @Test
  @DisplayName("Deve regsitrar multiplos pagamentos para meses futuro")
  public void shouldRegisterMultipleàymentsForFutureMonths() {

    PayamentDto paymentDto = new PayamentDto();
    paymentDto.setDescription("test boleto");
    paymentDto.setValue(new BigDecimal(23.4));
    paymentDto.setDue_date(LocalDate.now());
    Long repeticao = 7L;

    String accessToken = "";
    Users user = new Users();
    when(usersService.findByAccessToken(accessToken)).thenReturn(user);

    // Execução:
    List<Payment> response = payamentService.boletosRecorrentes(paymentDto, repeticao, accessToken);

    verify(paymentsRepository, times(repeticao.intValue())).save(paymentCaptor.capture());
    List<Payment> capturedPayments = paymentCaptor.getAllValues();

    assertEquals("test boleto", paymentCaptor.getValue().getDescription());
    assertEquals("23.40", paymentCaptor.getValue().getValue().toString());
    assertEquals(repeticao, capturedPayments.size());
    assertEquals(paymentDto.getDue_date(), capturedPayments.get(0).getDue_date());
    assertEquals(paymentDto.getDue_date().plusMonths(repeticao - 1),
        capturedPayments.get(capturedPayments.size() - 1).getDue_date());
    assertEquals(repeticao, response.size());
  }

  @Test
  @DisplayName("Deve registrar um pagamento")
  public void mustRegisterPayment() {
    PayamentDto paymentDto = new PayamentDto();
    paymentDto.setDescription("test boleto");
    paymentDto.setValue(new BigDecimal(23.4));
    paymentDto.setDue_date(LocalDate.now());

    String accessToken = "";
    Users user = new Users();

    when(usersService.findByAccessToken(accessToken)).thenReturn(user);
    // Execução:
    payamentService.register(paymentDto, accessToken);

    // Verificação:
    verify(paymentsRepository).save(paymentCaptor.capture());

    assertEquals("test boleto", paymentCaptor.getValue().getDescription());
    assertEquals("23.40", paymentCaptor.getValue().getValue().toString());
  }

  @Test
  @DisplayName("Deee computar o resumo")
  public void shouldComputeTheSummary() {

    LocalDate start = LocalDate.now();
    LocalDate finsh = start.plusDays(30);

    String accessToken = "";
    Users user = new Users();
    List<Payment> listPayments = listPayments();
    listPayments.get(1).setStatus(StatusBoleto.VENCIDO);

    listPayments.get(2).setValue(new BigDecimal("100.00"));
    listPayments.get(2).setStatus(StatusBoleto.PAGO);

    when(usersService.findByAccessToken(accessToken)).thenReturn(user);
    when(payamentService.periodTime(accessToken, "", start, finsh, "")).thenReturn(listPayments);

    // Execução:
    Sumary response = payamentService.sumary(accessToken, start, finsh);

    // Verificação:
    // total do periodo
    assertEquals("120.20", response.getTotalDue().toString());
    // ja pago
    assertEquals("100.00", response.getAmountPaid().toString());
    // resta pagar
    assertEquals("20.20", response.getRemaining().toString());
  }

  @Test
  @DisplayName("Deve trazer o total das categorias")
  public void mustComputerTheSumayByCategory() {

    LocalDate start = LocalDate.now();
    LocalDate finsh = start.plusDays(30);

    String accessToken = "";
    when(usersService.findByAccessToken(accessToken)).thenReturn(new Users());
    when(payamentService.periodTime(accessToken, "", start, finsh, "")).thenReturn(listPayments());

    // Execução:
    Map<String, BigDecimal> response = payamentService.sumaryCategory(accessToken, start, finsh);
    // Verificação
    assertEquals(2, response.size());
    assertEquals("20.20", response.get("casa").toString());
  }

  @Test
  @DisplayName("Deve trazer montante pago vazio se nenhuma conta tiver sido paga")
  public void mustBringZeroAmoundPaidIfNoBillHasBeenPaid() {

    LocalDate start = LocalDate.now();
    LocalDate finsh = start.plusDays(30);

    String accessToken = "";
    Users user = new Users();
    List<Payment> listPayments = listPayments();
    listPayments.get(1).setStatus(StatusBoleto.VENCIDO);

    listPayments.get(2).setValue(new BigDecimal("100.00"));
    listPayments.get(2).setStatus(StatusBoleto.EM_DIAS);

    when(usersService.findByAccessToken(accessToken)).thenReturn(user);
    when(payamentService.periodTime(accessToken, "", start, finsh, "")).thenReturn(listPayments);

    // Execução:
    Sumary response = payamentService.sumary(accessToken, start, finsh);

    // Verificação:
    // total do periodo
    assertEquals("120.20", response.getTotalDue().toString());
    // ja pago
    assertEquals("0.00", response.getAmountPaid().toString());
    // resta pagar
    assertEquals("120.20", response.getRemaining().toString());
  }

  @Test
  @DisplayName("Deve trazer restante vazio se todas as contas tiverem sido paga")
  public void mustBringAnZeroRemainingIfAllBillHaveBeenPaid() {

    LocalDate start = LocalDate.now();
    LocalDate finsh = start.plusDays(30);

    String accessToken = "";
    Users user = new Users();
    List<Payment> listPayments = listPayments();
    listPayments.get(0).setStatus(StatusBoleto.PAGO);
    listPayments.get(1).setStatus(StatusBoleto.PAGO);
    listPayments.get(2).setStatus(StatusBoleto.PAGO);

    when(usersService.findByAccessToken(accessToken)).thenReturn(user);
    when(payamentService.periodTime(accessToken, "", start, finsh, "")).thenReturn(listPayments);

    // Execução:
    Sumary response = payamentService.sumary(accessToken, start, finsh);

    // Verificação:
    // total do periodo
    assertEquals("30.30", response.getTotalDue().toString());
    // ja pago
    assertEquals("30.30", response.getAmountPaid().toString());
    // resta pagar
    assertEquals("0.00", response.getRemaining().toString());
  }

  private List<Payment> listPayments() {

    Payment payment_01 = new Payment();
    payment_01.setDescription("test boleto");
    payment_01.setValue(new BigDecimal(10.10));
    payment_01.setCategory(new Category("casa"));
    payment_01.setDue_date(LocalDate.now());
    payment_01.setStatus(StatusBoleto.EM_DIAS);

    Payment payment_02 = new Payment();
    payment_02.setDescription("test boleto");
    payment_02.setCategory(new Category("casa"));
    payment_02.setValue(new BigDecimal(10.10));
    payment_02.setDue_date(LocalDate.now());
    payment_02.setStatus(StatusBoleto.EM_DIAS);

    Payment payment_03 = new Payment();
    payment_03.setDescription("test boleto");
    payment_03.setValue(new BigDecimal(10.10));
    payment_03.setDue_date(LocalDate.now());

    payment_03.setCategory(new Category("estudo"));
    payment_03.setStatus(StatusBoleto.EM_DIAS);

    List<Payment> listPayments = new ArrayList<>();
    listPayments.add(payment_02);
    listPayments.add(payment_03);
    listPayments.add(payment_01);
    return listPayments;
  }

}
