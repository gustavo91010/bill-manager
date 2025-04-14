package com.ajudaqui.billmanager.controller;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import com.ajudaqui.billmanager.controller.from.BoletoFrom;
import com.ajudaqui.billmanager.entity.Payment;
import com.ajudaqui.billmanager.exception.MsgException;
import com.ajudaqui.billmanager.response.ApiPayment;
import com.ajudaqui.billmanager.response.ApiPayments;
import com.ajudaqui.billmanager.response.ApiResponse;
import com.ajudaqui.billmanager.service.PaymentService;
import com.ajudaqui.billmanager.service.vo.PayamentDto;

@RestController
@RequestMapping("/payment")
public class PaymentController {

  @Autowired
  private PaymentService paymentSerivce;
  private Logger logger = LoggerFactory.getLogger(PaymentController.class.getSimpleName());

  @PostMapping("/repeat/{repeat}")
  public ResponseEntity<?> boletosRecorrentes(@Valid @RequestBody PayamentDto payamentDto,
      @PathVariable("repeat") Long repeat,
      @RequestHeader("Authorization") String accessToken) {

    logger.info("[POST] | /payment/repet{repet} | accessToken: %d", accessToken);
    try {

      List<Payment> response = paymentSerivce.boletosRecorrentes(payamentDto, repeat, accessToken);

      return ResponseEntity.status(HttpStatus.CREATED).body(new ApiPayments(response));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage()));
    }

  }

  @GetMapping(value = "/id/{paymentId}") // ok
  public ResponseEntity<?> findById(@RequestHeader("Authorization") String accessToken,
      @PathVariable("paymentId") Long paymentId) {
    logger.info("[GET] | /payment/id/{paymentId} | accessToken: %s", accessToken);
    try {

      Payment boleto = paymentSerivce.findByIdForUsers(accessToken, paymentId);
      return ResponseEntity.ok(new PayamentDto(boleto));

    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage()));
    }

  }

  @GetMapping()
  public ResponseEntity<?> periodTime(
      @RequestHeader("Authorization") String accessToken,
      @RequestParam(value = "start") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate start,
      @RequestParam(value = "finsh") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate finsh,
      @RequestParam(value = "description", defaultValue = "") String description,
      @RequestParam(value = "status", defaultValue = "") String status) {
    logger.info("[GET] | /payment | accessToken: %s", accessToken);
    try {

      List<Payment> payments = paymentSerivce.periodTime(accessToken, description, start, finsh, status);

      return ResponseEntity.ok(new ApiPayments(payments));
    } catch (RuntimeException msg) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(msg.getMessage());
    }
  }

  @PutMapping("/{paymentId}")
  public ResponseEntity<?> atualizar(@RequestHeader("Authorization") String accessToken,
      @PathVariable("paymentId") Long paymentId,
      @RequestBody BoletoFrom from) {
    logger.info("[PUT] | /payment | accessToken: %s", accessToken);

    Payment paymentAtt = paymentSerivce.update(accessToken, paymentId, from);

    return new ResponseEntity<>(new ApiPayment(paymentAtt), HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> remover(@RequestHeader("Authorization") String accessToken,
      @PathVariable("paymentId") Long paymentId,
      @RequestBody BoletoFrom from) {

    logger.info("[DELETE] | /payment | accessToken: %s", accessToken);
    paymentSerivce.deleteById(accessToken, paymentId);
    return ResponseEntity.ok().build();

  }

}
