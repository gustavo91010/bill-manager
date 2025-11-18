package com.ajudaqui.billmanager.service;

import com.ajudaqui.billmanager.client.CalControlClient;
import com.ajudaqui.billmanager.client.model.MessageCalControl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CalControlService {

  private CalControlClient calControlService;

  @Value("${secretKey}")
  private String secretKey;

  public String send(MessageCalControl payload) {
    return calControlService.sendMessage(secretKey, payload);

  }

}
