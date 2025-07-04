package com.ajudaqui.billmanager.controller;

import java.time.LocalDate;
import java.util.List;

import com.ajudaqui.billmanager.controller.from.BoletoFrom;
import com.ajudaqui.billmanager.entity.Payment;
import com.ajudaqui.billmanager.response.ApiPayment;
import com.ajudaqui.billmanager.response.ApiPayments;
import com.ajudaqui.billmanager.response.ApiResponse;
import com.ajudaqui.billmanager.service.PaymentService;
import com.ajudaqui.billmanager.service.vo.PayamentDto;
import com.ajudaqui.billmanager.service.vo.Sumary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
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

@RestController
@RequestMapping("/payment")
@Validated
public class PaymentController {

  @Autowired
  private PaymentService paymentSerivce;

  @CrossOrigin
  @GetMapping("/health")
  public ApiResponse health() {
    return new ApiResponse("ok");
  }

  @CrossOrigin
  @GetMapping("/sumary")
  public Sumary getSumary(
      @RequestHeader("Authorization") String accessToken,
      @RequestParam(value = "start") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate start,
      @RequestParam(value = "finsh") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate finsh) {
    return paymentSerivce.sumary(accessToken, start, finsh);
  }

  @CrossOrigin
  @PostMapping("/repeat/{repeat}")
  public ResponseEntity<?> boletosRecorrentes(@RequestBody @Valid PayamentDto payamentDto,
      @PathVariable("repeat") Long repeat,
      @RequestHeader("Authorization") String accessToken) {
    try {
      List<Payment> response = paymentSerivce.boletosRecorrentes(payamentDto, repeat, accessToken);
      return ResponseEntity.status(HttpStatus.CREATED).body(new ApiPayments(response));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage()));
    }
  }

  @CrossOrigin
  @GetMapping(value = "/id/{id}") // ok
  public ResponseEntity<?> findById(@RequestHeader("Authorization") String accessToken,
      @PathVariable("id") Long paymentId) {
    try {

      Payment boleto = paymentSerivce.findByIdForUsers(accessToken, paymentId);
      return ResponseEntity.ok(new PayamentDto(boleto));
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage()));
    }
  }

  @CrossOrigin
  @GetMapping()
  public ResponseEntity<?> periodTime(
      @RequestHeader("Authorization") String accessToken,
      @RequestParam(value = "start") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate start,
      @RequestParam(value = "finsh") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate finsh,
      @RequestParam(value = "description", defaultValue = "") String description,
      @RequestParam(value = "status", defaultValue = "") String status) {
    try {

      List<Payment> payments = paymentSerivce.periodTime(accessToken, description, start, finsh, status);
      return ResponseEntity.ok(new ApiPayments(payments));
    } catch (RuntimeException msg) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(msg.getMessage()));
    }
  }

  @CrossOrigin
  @PutMapping("/confirm-paymeny/{id}")
  public ResponseEntity<?> confirmPayment(@RequestHeader("Authorization") String accessToken,
      @PathVariable("id") Long paymentId) {
    try {
      Payment paymentAtt = paymentSerivce.confirmPayment(accessToken, paymentId);
      return ResponseEntity.ok(new ApiPayment(paymentAtt));
    } catch (RuntimeException msg) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(msg.getMessage()));
    }
  }

  @CrossOrigin
  @PutMapping("/{id}")
  public ResponseEntity<?> atualizar(@RequestHeader("Authorization") String accessToken,
      @PathVariable("id") Long paymentId,
      @RequestBody BoletoFrom from) {

    try {
      Payment paymentAtt = paymentSerivce.update(accessToken, paymentId, from);
      return ResponseEntity.ok(new ApiPayment(paymentAtt));
    } catch (RuntimeException msg) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(msg.getMessage()));
    }
  }

  @CrossOrigin
  @DeleteMapping("/{id}")
  public ResponseEntity<?> remover(@RequestHeader("Authorization") String accessToken,
      @PathVariable("id") Long paymentId) {
    try {
      paymentSerivce.deleteById(accessToken, paymentId);

      return ResponseEntity.ok(new ApiResponse("Pagamento excluido com sucesso."));
    } catch (RuntimeException msg) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(msg.getMessage()));
    }
  }
}
