package com.ajudaqui.billmanager.service.filter.payments;

import com.ajudaqui.billmanager.entity.Payment;
import com.ajudaqui.billmanager.repository.PaymentsRepository;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class FilterSelection {

  public static List<Payment> searchFilter(PaymentsRepository repository, String accessToken, String description,
      LocalDate start,
      LocalDate finish, String status) {

    PaymentSearcheStrategy selectetd = new DescriptionAndStatus(
        new SearchByDescription(
            new SearchByStatus(
                new SearchByDateRange())));

    return selectetd.search(repository, accessToken, description, start, finish, status)
        .stream()
        .sorted(Comparator.comparing(Payment::getDueDate))
        .collect(Collectors.toList());
  }
}
