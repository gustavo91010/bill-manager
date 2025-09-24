package com.ajudaqui.billmanager.client.kafka.service;

import com.ajudaqui.billmanager.client.kafka.repository.ErrorMessageRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ErrorMessageService {

  @Autowired
  private ErrorMessageRepository repository;
  
}
