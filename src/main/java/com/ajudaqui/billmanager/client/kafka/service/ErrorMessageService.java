package com.ajudaqui.billmanager.client.kafka.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.transaction.Transactional;

import com.ajudaqui.billmanager.client.kafka.entity.ErrorMessage;
import com.ajudaqui.billmanager.client.kafka.repository.ErrorMessageRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ErrorMessageService {

  @Autowired
  private ErrorMessageRepository repository;

  @Transactional
  public ErrorMessage create(String topic, Map<String, Object> message) {
    if (message.get("accessToken") == null)
      message.put("accessToken", UUID.randomUUID().toString());
    return repository.save(new ErrorMessage(topic, message.get("accessToken").toString(), message));
  }

  public List<ErrorMessage> findAll() {
    return repository.findAll();
  }

  public void delete(Long id) {
    repository.deleteById(id);
  }

}
