package com.ajudaqui.billmanager.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.ajudaqui.billmanager.entity.Payment;
import com.ajudaqui.billmanager.entity.Users;
import com.ajudaqui.billmanager.repository.PaymentsRepository;
import com.ajudaqui.billmanager.service.vo.PayamentDto;

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
  @DisplayName("Não deve ferificar os boletos do mes se a lista estiver vazia")
  public void test() {

    PayamentDto paymentDto = new PayamentDto();
    paymentDto.setDescription("test boleto");
    paymentDto.setValue(new BigDecimal(23.4));
    paymentDto.setDue_date(LocalDate.now());

    String accessToken = "";
    Users user = new Users();

    List<Payment> payments = new ArrayList<>();

    when(usersService.findByAccessToken(accessToken)).thenReturn(user);
    when(payamentService
        .findAllMonth(user.getId(), paymentDto.getDue_date().getMonthValue(),
            paymentDto.getDue_date().getYear()))
        .thenReturn(payments);

    // Execução:
    payamentService.register(paymentDto, accessToken);

    // Verificação:
    assertTrue(paymentDto.getDue_date().getMonthValue() >= 1 && paymentDto.getDue_date().getMonthValue() <= 12);

    verify(payamentService, times(0)).findAllMonth(
        any(),
        argThat(i -> i >= 1 && i <= 12), // Limitando o intervalo de 1 a 12 para o mês
        anyInt());
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

    List<Payment> payments = new ArrayList<>();

    when(usersService.findByAccessToken(accessToken)).thenReturn(user);
    // when(payamentService.findAllMonth(any(), anyInt(), anyInt()))
    when(payamentService
        .findAllMonth(user.getId(), paymentDto.getDue_date().getMonthValue(),
            paymentDto.getDue_date().getYear()))
        .thenReturn(payments);
    // Execução:
    payamentService.register(paymentDto, accessToken);

    // Verificação:
    verify(paymentsRepository).save(paymentCaptor.capture());

    assertEquals("test boleto", paymentCaptor.getValue().getDescription());
    assertEquals("23.40", paymentCaptor.getValue().getValue().toString());
    // verify(payamentService, times(0)).findAllMonth(any(), anyInt(), anyInt());
    // verify(payamentService, times(0)).findAllMonth(user.getId(),
    // paymentDto.getDue_date().getMonthValue(),
    // paymentDto.getDue_date().getYear());
  }

}
