package com.ajudaqui.billmanager.service.filter.payments;

import java.time.LocalDate;
import java.util.List;

import com.ajudaqui.billmanager.entity.Payment;
import com.ajudaqui.billmanager.repository.PaymentsRepository;
import com.ajudaqui.billmanager.utils.StatusBoleto;

public class DescriptionAndStatus extends PaymentSearcheStrategy {

  public DescriptionAndStatus(PaymentSearcheStrategy next) {
    super(next);
  }

  @Override
  public List<Payment> search(PaymentsRepository repository, String accessToken, String description, LocalDate start,
      LocalDate finish, String status) {
    if (!description.isEmpty() && !status.isEmpty())
      return repository.findPayaments(accessToken, description, start, finish,
          StatusBoleto.valueOf(status));
    return next.search(repository, accessToken, description, start, finish, status);
  }
}
