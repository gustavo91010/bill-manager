package com.ajudaqui.billmanager.service.filter.payments;

import java.time.LocalDate;
import java.util.List;

import com.ajudaqui.billmanager.entity.Payment;
import com.ajudaqui.billmanager.repository.PaymentsRepository;

public abstract class PaymentSearcheStrategy {

  protected PaymentSearcheStrategy next;

  public PaymentSearcheStrategy(PaymentSearcheStrategy next) {
    this.next = next;
  }

  public abstract List<Payment> search(PaymentsRepository repository, String accessToken, String description,
      LocalDate start, LocalDate finish, String status);

}
