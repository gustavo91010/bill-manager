package com.ajudaqui.bill.manager.controller;

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

import com.ajudaqui.bill.manager.dto.PayamentDto;
import com.ajudaqui.bill.manager.entity.Payment;
import com.ajudaqui.bill.manager.entity.Vo.PayamentVO;
import com.ajudaqui.bill.manager.from.BoletoFrom;
import com.ajudaqui.bill.manager.service.PayamentService;
import com.ajudaqui.bill.manager.validacao.ApiPayments;

@RestController
@RequestMapping("/payament")
public class PayamentsController {

	@Autowired
	private PayamentService payamentSerivce;
	private static final Logger LOGGER = LoggerFactory.getLogger(PayamentsController.class.getSimpleName());


	@PutMapping("/pagamento/{id}")
	public ResponseEntity<?> makePayment(@PathVariable("id") Long id) {
		Payment boleto = payamentSerivce.makePayment(id);

		return new ResponseEntity<>(boleto, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<?> cadastrar(@RequestBody PayamentDto payamentDto) {
		try {

		 payamentSerivce.cadastrar(payamentDto);

			return new ResponseEntity<>("Pagamento cadastrado com sucesso.",
					HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>("Pagamento não cadastrado",
					HttpStatus.BAD_REQUEST);
		}

	}

	@PostMapping("/repet/{repet}")
	public ResponseEntity<?> boletosRecorrentes(@RequestBody PayamentDto payamentDto, @PathVariable("repet") Long repet) {

		try {

			payamentSerivce.boletosRecorrentes(payamentDto, repet);

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
			return ResponseEntity.ok(new PayamentVO(boleto));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Ocorreu um erro ao consultar o boleto.");
		}

	}

	@GetMapping(value = "/descricao/{descricao}")
	public ResponseEntity<?> consultarPorDescricao(@PathVariable("descricao") String descricao) {
		try {
			// TODO adicionar o id do usuario

			List<PayamentVO> boletosVO = payamentSerivce.findByDescricao(descricao);

			return ResponseEntity.ok(boletosVO);
		} catch (RuntimeException e) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Ocorreu um erro ao consultar o boleto.");
		}
	}

	@GetMapping("/dinamico")
//	@ApiOperation(value = "Faz uma consulta com dados variados sobre o boleto" )
	List<PayamentVO> findByBuscaDinamica(@RequestBody BoletoFrom payamentFrom) {

		// Falta testar
		List<PayamentVO> boletosVO = payamentSerivce.findAll(payamentFrom);

		return boletosVO;
	};
	
	@GetMapping(value = "/all/id/{id}")
	public ResponseEntity<?> findAll(@PathVariable("id") Long id
			,@RequestParam(value= "month", defaultValue="0") Integer month,
			@RequestParam(value= "year",defaultValue="0") Integer year,
			@RequestParam("status") String status) {
	//	try {
		System.err.println("controller");

		List<Payment> payments = payamentSerivce.searcheByMonthAndStatus(id, month, year, status);
		LOGGER.info("busca do id {} realizado com sucesso.", id);

			return ResponseEntity.ok(new ApiPayments(payments));
		//} catch (RuntimeException e) {			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)					.body("Ocorreu um erro com a consulta.");		}
	}

	@PutMapping("/{id}")
//	@ApiOperation(value = "Atualiza qualquer dado de um boleto" )
	public ResponseEntity<PayamentVO> atualizar(@PathVariable Long id, @RequestBody BoletoFrom from) {

		// Falta testar
		Payment boleto = payamentSerivce.findById(id);
		if (!(boleto == null)) {
			boleto.setDescription( (from.getDescricao()));
			boleto.setValue(from.getValor());
			boleto.setDue_date(from.getVencimento());

			return ResponseEntity.ok(new PayamentVO(boleto));
		}
		return ResponseEntity.notFound().build();
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
		try {
			payamentSerivce.resumoDoMesXlsx(nome);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
