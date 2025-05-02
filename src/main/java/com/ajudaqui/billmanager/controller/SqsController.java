package com.ajudaqui.billmanager.controller;

import com.ajudaqui.billmanager.response.ApiResponse;
import com.ajudaqui.billmanager.service.sqs.SqsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sqs")
public class SqsController {

  @Autowired
  private SqsService sqsProducerService;

  @PostMapping("/send-message/{fila}")
  public ApiResponse senMessage(@PathVariable(required = true) String fila, @RequestBody String message) {
    sqsProducerService.sendMessage(fila, message);
    return new ApiResponse("Messagem enviada com sucesso!");
  }

}
