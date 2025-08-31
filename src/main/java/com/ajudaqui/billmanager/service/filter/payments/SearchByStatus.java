package com.ajudaqui.billmanager.service.filter.payments;

import java.time.LocalDate;
import java.util.List;

import com.ajudaqui.billmanager.entity.Payment;
import com.ajudaqui.billmanager.repository.PaymentsRepository;
import com.ajudaqui.billmanager.utils.StatusBoleto;

public class SearchByStatus extends PaymentSearcheStrategy {

  public SearchByStatus(PaymentSearcheStrategy next) {
    super(next);
  }

  @Override
  public List<Payment> search(PaymentsRepository repository, String accessToken, String description, LocalDate start,
      LocalDate finish, String status) {
    if (!status.isEmpty())
      repository.findPayaments(accessToken, start, finish, StatusBoleto.valueOf(status));

    return next.search(repository, accessToken, description, start, finish, status);
  }
}
