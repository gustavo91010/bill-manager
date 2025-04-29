package com.ajudaqui.billmanager.controller;

import java.util.List;

import com.ajudaqui.billmanager.response.ApiResponse;
import com.ajudaqui.billmanager.response.ApiResponseList;
import com.ajudaqui.billmanager.service.sqs.QueueService;
import com.ajudaqui.billmanager.service.sqs.SqsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

// @RestController
// @RequestMapping("/sqs")
public class SqsController {

  @Autowired
  private SqsService sqsProducerService;

  @Autowired
  private QueueService queueService;

  @PostMapping("/send-message/{fila}")
  public ApiResponse senMessage(@PathVariable(required = true) String fila, @RequestBody String message) {
    sqsProducerService.sendMessage(fila, message);
    return new ApiResponse("Messagem enviada com sucesso!");
  }

  @PostMapping("/queue-create")
  public ApiResponse createQueue(@RequestParam String queueName) {

    String response = queueService.createQueue(queueName);
    return new ApiResponse(response);
  }

  @GetMapping("/queue-list")
  public ApiResponseList queueList() {
    List<String> response = queueService.queueList();
    return new ApiResponseList(response);
  }

  @DeleteMapping("/queue-delete")
  public ApiResponse delete(@RequestParam String queueName) {
    String response = queueService.deleteQueue(queueName);
    return new ApiResponse(response);

  }

}
