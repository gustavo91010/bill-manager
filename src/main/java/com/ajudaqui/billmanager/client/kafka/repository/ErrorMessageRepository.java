package com.ajudaqui.billmanager.client.kafka.repository;

import com.ajudaqui.billmanager.client.kafka.entity.ErrorMessage;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ErrorMessageRepository extends JpaRepository<ErrorMessage, Long> {

}
