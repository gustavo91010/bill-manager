package com.ajudaqui.billmanager.controller;

import java.io.IOException;
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
import com.ajudaqui.billmanager.service.PayamentService;
import com.ajudaqui.billmanager.service.vo.PayamentDto;
import com.ajudaqui.billmanager.utils.validacao.ApiPayments;

@RestController
@RequestMapping("/payament")
public class PaymentController {

	@Autowired
	private PayamentService payamentSerivce;
	private static final Logger LOGGER = LoggerFactory.getLogger(PaymentController.class.getSimpleName());




	@PostMapping("/{userId}")
	public ResponseEntity<?> cadastrar(@RequestBody PayamentDto payamentDto, @PathVariable("userId") Long userId) {
//		try {

		 payamentSerivce.cadastrar(payamentDto,userId);

			return new ResponseEntity<>("Pagamento cadastrado com sucesso.",
					HttpStatus.CREATED);
//		} catch (Exception e) {
//			return new ResponseEntity<>("Pagamento não cadastrado",
//					HttpStatus.BAD_REQUEST);
//		}

	}

	@PostMapping("/{repet}/{userId}")
	public ResponseEntity<?> boletosRecorrentes(@RequestBody PayamentDto payamentDto, @PathVariable("repet") Long repet, @PathVariable("userId") Long userId) {

		try {

			payamentSerivce.boletosRecorrentes(payamentDto, repet, userId);

			return ResponseEntity.status(HttpStatus.CREATED).body("Boleto cadastrado com sucesso");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Não cadastrado");
		}

	}

	@GetMapping(value = "/id/{id}")
	public ResponseEntity<?> consultarPorId(@PathVariable("id") Long id) {
		try {
// TODO adicionar o id do usuario
			Payment boleto = payamentSerivce.findById(id);
			return ResponseEntity.ok(new PayamentDto(boleto));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Ocorreu um erro ao consultar o boleto.");
		}

	}

	@GetMapping(value = "/descricao/{descricao}")
	public ResponseEntity<?> consultarPorDescricao(@PathVariable("descricao") String descricao) {
		try {
			// TODO adicionar o id do usuario

			List<PayamentDto> boletosVO = payamentSerivce.findByDescricao(descricao);

			return ResponseEntity.ok(boletosVO);
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
	
	@GetMapping(value = "/all/id/{id}")
	public ResponseEntity<?> findAll(@PathVariable("id") Long id
			,@RequestParam(value= "month", defaultValue="0") Integer month,
			@RequestParam(value= "year",defaultValue="0") Integer year,
			@RequestParam("status") String status) {
	//	try {

		List<Payment> payments = payamentSerivce.searcheByMonthAndStatus(id, month, year, status);
		LOGGER.info("busca do id {} realizado com sucesso.", id);

			return ResponseEntity.ok(new ApiPayments(payments));
		//} catch (RuntimeException e) {			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)					.body("Ocorreu um erro com a consulta.");		}
	}

	@PutMapping("/{id}")
//	@ApiOperation(value = "Atualiza qualquer dado de um boleto" )
	public ResponseEntity<PayamentDto> atualizar(@PathVariable Long id, @RequestBody BoletoFrom from) {

		// Falta testar
		Payment boleto = payamentSerivce.findById(id);
		if (!(boleto == null)) {
			boleto.setDescription( (from.getDescricao()));
			boleto.setValue(from.getValor());
			boleto.setDue_date(from.getVencimento());

			return ResponseEntity.ok(new PayamentDto(boleto));
		}
		return ResponseEntity.notFound().build();
	}
	@PutMapping("/pagamento/{id}")
	public ResponseEntity<?> makePayment(@PathVariable("id") Long id) {
		Payment boleto = payamentSerivce.makePayment(id);

		return new ResponseEntity<>(boleto, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
//	@ApiOperation(value = "Metodo para remover um boleto especifico" )
	public ResponseEntity<?> remover(@PathVariable Long id) {

		// Falta testar
		payamentSerivce.deleteById(id);
		return ResponseEntity.ok().build();

	}

	@PutMapping("/status-update")
	public ResponseEntity<?> atualizarSt() {

		try {
			payamentSerivce.performStatusUpdate();
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
