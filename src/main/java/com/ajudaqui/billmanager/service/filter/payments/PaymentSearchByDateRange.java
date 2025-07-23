package com.ajudaqui.billmanager.service.filter.payments;

import java.time.LocalDate;
import java.util.List;

import com.ajudaqui.billmanager.entity.Payment;
import com.ajudaqui.billmanager.repository.PaymentsRepository;

public class PaymentSearchByDateRange extends PaymentSearcheStrategy {

  public PaymentSearchByDateRange() {
    super(null);
  }

  @Override
  public List<Payment> search(PaymentsRepository repository, String accessToken, String description, LocalDate start,
      LocalDate finish, String status) {
    return next.search(repository, accessToken, description, start, finish, status);
  }

}
