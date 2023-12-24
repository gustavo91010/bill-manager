package com.ajudaqui.billmanager.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ajudaqui.billmanager.controller.from.BoletoFrom;
import com.ajudaqui.billmanager.entity.Payment;
import com.ajudaqui.billmanager.exception.MsgException;
import com.ajudaqui.billmanager.response.ApiPayment;
import com.ajudaqui.billmanager.response.ApiPayments;
import com.ajudaqui.billmanager.service.PayamentService;
import com.ajudaqui.billmanager.service.vo.PayamentDto;

@RestController
@RequestMapping("/payment")
public class PaymentController {

	@Autowired
	private PayamentService paymentSerivce;
	private static final Logger LOGGER = LoggerFactory.getLogger(PaymentController.class.getSimpleName());

	@PostMapping("/{userId}") // ok
	public ResponseEntity<?> cadastrar(@RequestBody PayamentDto payamentDto, @PathVariable("userId") Long userId) {
		try {

			Payment payment = paymentSerivce.cadastrar(payamentDto, userId);

			return new ResponseEntity<>(new ApiPayment(payment), HttpStatus.CREATED);
		} catch (MsgException msg) {
			return new ResponseEntity<>(msg.getMessage(), HttpStatus.BAD_REQUEST);
		}

	}

	@PostMapping("/{repet}/{userId}") // ok
	public ResponseEntity<?> boletosRecorrentes(@RequestBody PayamentDto payamentDto, @PathVariable("repet") Long repet,
			@PathVariable("userId") Long userId) {

		try {

			paymentSerivce.boletosRecorrentes(payamentDto, repet, userId);

			return ResponseEntity.status(HttpStatus.CREATED).body("Boleto cadastrado com sucesso");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}

	}

	@GetMapping(value = "/usersId") // ok
	public ResponseEntity<?> consultarPorId(@RequestParam("usersId") Long usersId,
			@RequestParam("paymentId") Long paymentId) {
		try {

			Payment boleto = paymentSerivce.findByIdForUsers(usersId, paymentId);
			return ResponseEntity.ok(new PayamentDto(boleto));

		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}

	}

	@GetMapping(value = "/descricao") //ok
	public ResponseEntity<?> consultarPorDescricao(@RequestParam("usersId") Long usersId,
			@RequestParam("description") String description) {
		try {

			List<Payment> payments = paymentSerivce.findByDescricao(usersId, description);

			return ResponseEntity.ok(new ApiPayments(payments));
		} catch (RuntimeException e) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Ocorreu um erro ao consultar o boleto.");
		}
	}

	@GetMapping("/dinamico")
//	@ApiOperation(value = "Faz uma consulta com dados variados sobre o boleto" )
	List<PayamentDto> findByBuscaDinamica(@RequestBody BoletoFrom payamentFrom) {

		// Falta testar
//		List<PayamentDto> boletosVO = payamentSerivce.findAll(payamentFrom);

		return null;
	};

//	specific search
	@GetMapping(value = "/search") // ok
	public ResponseEntity<?> searcheByUsersByMonthAndStatus(
			@RequestParam("usersId") Long usersId,
			@RequestParam(value = "month", defaultValue = "0") Integer month,
			@RequestParam(value = "year", defaultValue = "0") Integer year,
			@RequestParam("status") String status) {
		 try {

		List<Payment> payments = paymentSerivce.searcheByUsersByMonthAndStatus(usersId, month, year, status);
		LOGGER.info("pagamentos do user com  id {} realizado com sucesso.", usersId);

		return ResponseEntity.ok(new ApiPayments(payments));
		 } catch (RuntimeException msg) { return
		 ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) .body(msg.getMessage()); }
	}

	@GetMapping(value = "/month") // ok
	public ResponseEntity<?> findAllMonth(@RequestParam(value = "usersId") Long usersId,
			@RequestParam(value = "month") Integer month, @RequestParam(value = "year") Integer year) {
		try {
			List<Payment> payments = paymentSerivce.findAllMonth(usersId, month, year);
			return ResponseEntity.ok(new ApiPayments(payments));

		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@PutMapping("/{userId}/{paymentId}")
	public ResponseEntity<?> atualizar(@PathVariable("userId") Long userId,@PathVariable("paymentId") Long paymentId, @RequestBody BoletoFrom from) {
		Payment paymentAtt = paymentSerivce.update(userId, paymentId, from);
	
		return new ResponseEntity<>(new ApiPayment(paymentAtt), HttpStatus.OK);
	}

	@PutMapping("/pagamento/{id}")
	public ResponseEntity<?> makePayment(@PathVariable("id") Long id) {
		Payment boleto = paymentSerivce.makePayment(id);

		return new ResponseEntity<>(boleto, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> remover(@PathVariable Long id) {

		// Falta testar
		paymentSerivce.deleteById(id);
		return ResponseEntity.ok().build();

	}

	@PutMapping("/status-update")
	public ResponseEntity<?> atualizarSt() {

		try {
			paymentSerivce.performStatusUpdate();
			return ResponseEntity.ok().build();

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ocorreu um erro ao consultar o boleto.");
		}
	}

	@PostMapping("/xlsx/{nome}")
	public void resumoDoMesXlsx(@PathVariable("nome") String nome) {
//		try {
//			payamentSerivce.resumoDoMesXlsx(nome);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

}