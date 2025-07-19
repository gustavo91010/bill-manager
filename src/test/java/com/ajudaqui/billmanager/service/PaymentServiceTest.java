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

  @Captor
  private ArgumentCaptor<Payment> paymentCaptor;

  @Test
  @DisplayName("Deve regsitrar uma vez com o recorrente")
   void mustRegisterOnceWithTheApplicant() {

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
    assertEquals(paymentDto.getDue_date(), capturedPayments.get(0).getDueDate());
    assertEquals(paymentDto.getDue_date().plusMonths(repeticao - 1),
        capturedPayments.get(capturedPayments.size() - 1).getDueDate());
    assertEquals(repeticao, response.size());
  }

  @Test
  @DisplayName("Deve regsitrar multiplos pagamentos para meses futuro")
   void shouldRegisterMultipleàymentsForFutureMonths() {

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
    assertEquals(paymentDto.getDue_date(), capturedPayments.get(0).getDueDate());
    assertEquals(paymentDto.getDue_date().plusMonths(repeticao - 1),
        capturedPayments.get(capturedPayments.size() - 1).getDueDate());
    assertEquals(repeticao, response.size());
  }

  @Test
  @DisplayName("Deve registrar um pagamento")
   void mustRegisterPayment() {
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
   void shouldComputeTheSummary() {

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
   void mustComputerTheSumayByCategory() {

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
   void mustBringZeroAmoundPaidIfNoBillHasBeenPaid() {

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
   void mustBringAnZeroRemainingIfAllBillHaveBeenPaid() {

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

    Payment payment01 = new Payment();
    payment01.setDescription("test boleto");
    payment01.setValue(new BigDecimal(10.10));
    payment01.setCategory(new Category("casa"));
    payment01.setDueDate(LocalDate.now());
    payment01.setStatus(StatusBoleto.EM_DIAS);

    Payment payment02 = new Payment();
    payment02.setDescription("test boleto");
    payment02.setCategory(new Category("casa"));
    payment02.setValue(new BigDecimal(10.10));
    payment02.setDueDate(LocalDate.now());
    payment02.setStatus(StatusBoleto.EM_DIAS);

    Payment payment03 = new Payment();
    payment03.setDescription("test boleto");
    payment03.setValue(new BigDecimal(10.10));
    payment03.setDueDate(LocalDate.now());

    payment03.setCategory(new Category("estudo"));
    payment03.setStatus(StatusBoleto.EM_DIAS);


    List<Payment> listPayments= new ArrayList<>();
    listPayments.add(payment02);
    listPayments.add(payment03);
    listPayments.add(payment01);
    return listPayments;
  }

}
