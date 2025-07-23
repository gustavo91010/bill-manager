package com.ajudaqui.billmanager.service.filter.payments;

import java.time.LocalDate;
import java.util.List;

import com.ajudaqui.billmanager.entity.Payment;
import com.ajudaqui.billmanager.repository.PaymentsRepository;

public class PaymentSearchByDescription extends PaymentSearcheStrategy {

  public PaymentSearchByDescription(PaymentSearcheStrategy next) {
    super(next);
  }

  @Override
  public List<Payment> search(PaymentsRepository repository, String accessToken, String description, LocalDate start,
      LocalDate finish, String status) {
    if (!description.isEmpty())
      repository.findPayaments(accessToken, description, start, finish);
    return next.search(repository, accessToken, description, start, finish, status);
  }
}
