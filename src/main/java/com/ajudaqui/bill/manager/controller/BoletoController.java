package com.ajudaqui.bill.manager.controller;

import java.io.IOException;
import java.net.URI;
import java.util.List;

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
import org.springframework.web.util.UriComponentsBuilder;

import com.ajudaqui.bill.manager.dto.BoletoDto;
import com.ajudaqui.bill.manager.entity.Boleto;
import com.ajudaqui.bill.manager.entity.Vo.BoletoVO;
import com.ajudaqui.bill.manager.from.BoletoFrom;
import com.ajudaqui.bill.manager.service.BoletoService;

@RestController
@RequestMapping("/boletos")
public class BoletoController {

	@Autowired
	private BoletoService boletoSerivce;

	@PutMapping("/pagamento/{id}")
	public ResponseEntity<?> pagamento(@PathVariable("id") Long id) {
		Boleto boleto = boletoSerivce.pagamento(id);

		return new ResponseEntity<>(boleto, HttpStatus.OK);
	}

	@PostMapping
//	@ApiOperation(value = "Salva um novo boleto no banco de dados" )
	public ResponseEntity<?> cadastrar(@RequestBody BoletoDto boletoDto, UriComponentsBuilder uriBuilder) {

		try {

			Boleto boleto = boletoSerivce.cadastrar(boletoDto);

			URI uri = uriBuilder.path("/boletos").buildAndExpand(boleto.getId()).toUri();
			return ResponseEntity.created(uri).body(new BoletoVO(boleto));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Não cadastrado");
		}

	}

	@PostMapping("/recorrente/{repet}")
	public ResponseEntity<?> boletosRecorrentes(@RequestBody BoletoDto boletoDto, @PathVariable("repet") Long repet,
			UriComponentsBuilder uriBuilder) {

		try {

			boletoSerivce.boletosRecorrentes(boletoDto, repet);

			return ResponseEntity.status(HttpStatus.CREATED).body("Boleto cadastrado com sucesso");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Não cadastrado");
		}

	}

	@GetMapping(value = "/id/{id}")
//	@ApiOperation(value = "Chama os boletos filtrando pelo Id" )
	public ResponseEntity<?> consultarPorId(@PathVariable("id") Long id) {
		try {

			Boleto boleto = boletoSerivce.findById(id);
			return ResponseEntity.ok(new BoletoVO(boleto));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Ocorreu um erro ao consultar o boleto.");
		}

	}

	@GetMapping(value = "/descricao/{descricao}")
//	@ApiOperation(value = "Chama os boletos filtrando pela Descrição" )
	public ResponseEntity<?> consultarPorDescricao(@PathVariable("descricao") String descricao) {
		try {
			List<BoletoVO> boletosVO = boletoSerivce.findByDescricao(descricao);

			return ResponseEntity.ok(boletosVO);
		} catch (RuntimeException e) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Ocorreu um erro ao consultar o boleto.");
		}
	}

	@GetMapping(value = "/pago")
//	@ApiOperation(value = "Mostra os boletos que ja foram pago " )
	public ResponseEntity<?> mostrarBoletosPagos(@RequestParam int mes, @RequestParam int ano) {
		try {

			List<BoletoVO> boletosVO = boletoSerivce.findBoletosPagos(mes, ano);

			return ResponseEntity.ok(boletosVO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ocorreu um erro ao consultar o boleto.");
		}
	}

	@GetMapping(value = "/vencido")
//	@ApiOperation(value = "Chama os boletos que estao vencidos" )
	public ResponseEntity<?> mostrarBoletosVencidos() {
		try {
			List<BoletoVO> boletosVO = boletoSerivce.findBoletosVencidos();

			return ResponseEntity.ok(boletosVO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ocorreu um erro ao consultar o boleto.");

		}
	}

	@GetMapping(value = "/data")
//	@ApiOperation(value = "Chama os boletos do no mes " )
	public ResponseEntity<?> consultarBoletosMes(@RequestParam int mes, @RequestParam int ano) {
		try {
			List<BoletoVO> boletosVO = boletoSerivce.findBoletosDoMes(mes, ano);

			return ResponseEntity.ok(boletosVO);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ocorreu um erro ao consultar o boleto.");
		}

	}

	@GetMapping(value = "/a-serem-pagos-no-mes")
//	@ApiOperation(value = "Chama os boletos a serem pagos no mes atual" )
	public ResponseEntity<?> consultarBoletosASeremPagosMes(@RequestParam int mes, @RequestParam int ano) {

		try {
			List<BoletoVO> boletosVO = boletoSerivce.findBoletosASeremPagosNoMes(mes, ano);

			return ResponseEntity.ok(boletosVO);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ocorreu um erro ao consultar o boleto.");
		}
	}

	@GetMapping("/dinamico")
//	@ApiOperation(value = "Faz uma consulta com dados variados sobre o boleto" )
	List<BoletoVO> findByBuscaDinamica(@RequestBody BoletoFrom boletoFrom) {

		// Falta testar
		List<BoletoVO> boletosVO = boletoSerivce.findAll(boletoFrom);

		return boletosVO;

	};

	@PutMapping("/{id}")
//	@ApiOperation(value = "Atualiza qualquer dado de um boleto" )
	public ResponseEntity<BoletoVO> atualizar(@PathVariable Long id, @RequestBody BoletoFrom from) {

		// Falta testar
		Boleto boleto = boletoSerivce.findById(id);
		if (!(boleto == null)) {
			boleto.setDescricao(from.getDescricao());
			boleto.setValor(from.getValor());
			boleto.setVencimento(from.getVencimento());

			return ResponseEntity.ok(new BoletoVO(boleto));
		}
		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{id}")
//	@ApiOperation(value = "Metodo para remover um boleto especifico" )
	public ResponseEntity<?> remover(@PathVariable Long id) {

		// Falta testar
		boletoSerivce.deleteById(id);
		return ResponseEntity.ok().build();

	}

	@PutMapping("/status-update")
	public ResponseEntity<?> atualizarSt() {

		try {
			boletoSerivce.performStatusUpdate();
			return ResponseEntity.ok().build();

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ocorreu um erro ao consultar o boleto.");
		}
	}

	@PostMapping("/xlsx/{nome}")
	public void resumoDoMesXlsx(@PathVariable("nome") String nome) {
		try {
			boletoSerivce.resumoDoMesXlsx(nome);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
