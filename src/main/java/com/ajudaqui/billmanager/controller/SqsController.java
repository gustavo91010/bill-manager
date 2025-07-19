package com.ajudaqui.billmanager.controller;

import com.ajudaqui.billmanager.entity.Users;
import com.ajudaqui.billmanager.response.ApiResponse;
import com.ajudaqui.billmanager.service.sqs.SqsService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

// @RestController
// @RequestMapping("/sqs")
public class SqsController {

  private SqsService sqsProducerService;

  public SqsController(SqsService sqsProducerService) {
    this.sqsProducerService = sqsProducerService;
  }

  @CrossOrigin
  @PostMapping("/signin")
  ResponseEntity<Users> signin(
      @RequestHeader("Authorization") String authorization) {

    return ResponseEntity.ok(sqsProducerService.create(authorization));
  }

  @PostMapping("/send-message/{fila}")
  public ApiResponse senMessage(@PathVariable(required = true) String fila, @RequestBody String message) {
    sqsProducerService.sendMessage(fila, message);
    return new ApiResponse("Messagem enviada com sucesso!");
  }

}
