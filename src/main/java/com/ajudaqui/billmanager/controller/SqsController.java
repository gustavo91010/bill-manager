package com.ajudaqui.billmanager.controller;

import com.ajudaqui.billmanager.service.SqsProducerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sqs")
public class SqsController {

  @Autowired
  private SqsProducerService sqsProducerService;

  @PostMapping("/send-message")
  public String senMessage(@RequestBody String message) {
    sqsProducerService.sendMessage(message);
    return message;
  }
}
