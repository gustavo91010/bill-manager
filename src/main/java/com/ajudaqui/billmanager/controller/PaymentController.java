package com.ajudaqui.billmanager.controller;

import java.time.LocalDate;
import java.util.List;

import com.ajudaqui.billmanager.controller.from.BoletoFrom;
import com.ajudaqui.billmanager.entity.Payment;
import com.ajudaqui.billmanager.response.ApiPayment;
import com.ajudaqui.billmanager.response.ApiPayments;
import com.ajudaqui.billmanager.response.ApiResponse;
import com.ajudaqui.billmanager.response.ApiSumaryCategory;
import com.ajudaqui.billmanager.service.PaymentService;
import com.ajudaqui.billmanager.service.vo.PayamentDto;
import com.ajudaqui.billmanager.service.vo.Sumary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
  @GetMapping("/sumary-category")
  public ResponseEntity<?> getSumaryCategory(
      @RequestHeader("Authorization") String accessToken,
      @RequestParam(value = "start") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate start,
      @RequestParam(value = "finsh") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate finsh) {
    // return ResponseEntity.ok(paymentSerivce.sumaryCategory(accessToken, start, finsh));
    return ResponseEntity.ok(new ApiSumaryCategory(paymentSerivce.sumaryCategory(accessToken, start, finsh)));
  }

  @CrossOrigin
  @PostMapping("/repeat/{repeat}")
  public ResponseEntity<?> boletosRecorrentes(@RequestBody @Valid PayamentDto payamentDto,
      @PathVariable("repeat") Long repeat,
      @RequestHeader("Authorization") String accessToken) {
    List<Payment> response = paymentSerivce.boletosRecorrentes(payamentDto, repeat, accessToken);
    return ResponseEntity.status(HttpStatus.CREATED).body(new ApiPayments(response));
  }

  @CrossOrigin
  @GetMapping(value = "/id/{id}") // ok
  public ResponseEntity<?> findById(@RequestHeader("Authorization") String accessToken,
      @PathVariable("id") Long paymentId) {
    return ResponseEntity.ok(new PayamentDto(paymentSerivce.findByIdForUsers(accessToken, paymentId)));
  }

  @CrossOrigin
  @GetMapping()
  public ResponseEntity<?> periodTime(
      @RequestHeader("Authorization") String accessToken,
      @RequestParam(value = "start") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate start,
      @RequestParam(value = "finsh") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate finsh,
      @RequestParam(value = "description", defaultValue = "") String description,
      @RequestParam(value = "status", defaultValue = "") String status) {
    List<Payment> payments = paymentSerivce.periodTime(accessToken, description, start, finsh, status);
    return ResponseEntity.ok(new ApiPayments(payments));
  }

  @CrossOrigin
  @PutMapping("/confirm-paymeny/{id}")
  public ResponseEntity<?> confirmPayment(@RequestHeader("Authorization") String accessToken,
      @PathVariable("id") Long paymentId) {
    Payment paymentAtt = paymentSerivce.confirmPayment(accessToken, paymentId);
    return ResponseEntity.ok(new ApiPayment(paymentAtt));
  }

  @CrossOrigin
  @PutMapping("/{id}")
  public ResponseEntity<?> update(@RequestHeader("Authorization") String accessToken,
      @PathVariable("id") Long paymentId,
      @RequestBody BoletoFrom from) {
    Payment paymentAtt = paymentSerivce.update(accessToken, paymentId, from);
    return ResponseEntity.ok(new ApiPayment(paymentAtt));
  }

  @CrossOrigin
  @PutMapping("/add/category")
  public ResponseEntity<?> addCategry(@RequestHeader("Authorization") String accessToken,
      @RequestParam Long paymentId,
      @RequestParam String category) {
    Payment paymentAtt = paymentSerivce.addCategory(accessToken, paymentId, category);
    return ResponseEntity.ok(new ApiPayment(paymentAtt));
  }

  @CrossOrigin
  @DeleteMapping("/{id}")
  public ResponseEntity<?> remover(@RequestHeader("Authorization") String accessToken,
      @PathVariable("id") Long paymentId) {
    paymentSerivce.deleteById(accessToken, paymentId);
    return ResponseEntity.ok(new ApiResponse("Pagamento excluido com sucesso."));
  }
}
