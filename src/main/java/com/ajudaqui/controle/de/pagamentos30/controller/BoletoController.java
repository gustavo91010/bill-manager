package com.ajudaqui.controle.de.pagamentos30.controller;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
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

import com.ajudaqui.controle.de.pagamentos30.dto.BoletoDto;
import com.ajudaqui.controle.de.pagamentos30.entity.Boleto;
import com.ajudaqui.controle.de.pagamentos30.entity.StatusBoleto;
import com.ajudaqui.controle.de.pagamentos30.entity.ValidarStatus;
import com.ajudaqui.controle.de.pagamentos30.entity.Vo.BoletoVO;
import com.ajudaqui.controle.de.pagamentos30.from.BoletoFrom;
import com.ajudaqui.controle.de.pagamentos30.service.BoletoService;
import com.ajudaqui.controle.de.pagamentos30.specification.BoletoSpecification;

@RestController
@RequestMapping("/boletos")
public class BoletoController {

	@Autowired
	private BoletoService boletoSerivce;

	@PostMapping
//	@ApiOperation(value = "Salva um novo boleto no banco de dados" )
	public ResponseEntity<BoletoVO> cadastrar(@RequestBody BoletoDto boletoDto, UriComponentsBuilder uriBuilder) {

		Boleto boleto = boletoSerivce.cadastrar(boletoDto);

		URI uri = uriBuilder.path("/boletos/{id}").buildAndExpand(boleto.getId()).toUri();
		return ResponseEntity.created(uri).body(new BoletoVO(boleto));

	}


	@GetMapping(value = "/id/{id}")
//	@ApiOperation(value = "Chama os boletos filtrando pelo Id" )
	public ResponseEntity<BoletoVO> consultarPorId(@PathVariable("id") Long id) {
		Boleto boleto = boletoSerivce.findById(id);
		return ResponseEntity.ok(new BoletoVO(boleto));

	}

	@GetMapping(value = "/descricao/{descricao}")
//	@ApiOperation(value = "Chama os boletos filtrando pela Descrição" )
	public ResponseEntity<List<BoletoVO>> consultarPorDescricao(@PathVariable("descricao") String descricao) {
		List<Boleto> boletos = boletoSerivce.findByDescricao(descricao);

		List<BoletoVO> boletosVO = new ArrayList<>();
		boletos.forEach(b -> {
			boletosVO.add(new BoletoVO(b));
		});
		if (boletosVO.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(boletosVO);

	}

	@GetMapping(value = "/pago")
//	@ApiOperation(value = "Mostra os boletos que ja foram pago " )
	public ResponseEntity<List<BoletoVO>> mostrarBoletosPagos(@RequestParam int mes, @RequestParam int ano) {
		List<BoletoVO> boletosVO = boletoSerivce.findBoletosPagos(mes, ano);

		return ResponseEntity.ok(boletosVO);
	}

	@GetMapping(value = "/vencido")
//	@ApiOperation(value = "Chama os boletos que estao vencidos" )
	public ResponseEntity<List<BoletoVO>> mostrarBoletosVencidos() {
		List<BoletoVO> boletosVO = boletoSerivce.findBoletosVencidos();

		return ResponseEntity.ok(boletosVO);
	}

	@GetMapping(value = "/mes")
//	@ApiOperation(value = "Chama os boletos do no mes " )
	public List<BoletoVO> consultarBoletosMes(@RequestParam int mes, @RequestParam int ano) {

		List<BoletoVO> boletosVO = boletoSerivce.findBoletosDoMes(mes, ano);

		return boletosVO;
	}

	@GetMapping(value = "/do-mes")
//	@ApiOperation(value = "Chama os boletos a serem pagos no mes atual" )
	public List<BoletoVO> consultarBoletosASeremPagosMes(@RequestParam int mes, @RequestParam int ano) {

		List<BoletoVO> boletosVO = boletoSerivce.findBoletosASeremPagosNoMes(mes, ano);

		return boletosVO;
	}

	@GetMapping("/dinamico")
//	@ApiOperation(value = "Faz uma consulta com dados variados sobre o boleto" )
	List<BoletoVO> findByBuscaDinamica(@RequestBody BoletoFrom boletoFrom) {

		List<BoletoVO> boletosVO = boletoSerivce.findAll(boletoFrom);

		return boletosVO;

	};

	@PutMapping("/{id}")
//	@ApiOperation(value = "Atualiza qualquer dado de um boleto" )
	public ResponseEntity<BoletoVO> atualizar(@PathVariable Long id, @RequestBody BoletoFrom from) {

		 Boleto boleto = boletoSerivce.findById(id);
		if (!(boleto== null)) {
			boleto.setDescricao(from.getDescricao());
			boleto.setValor(from.getValor());
			boleto.setVencimento(from.getVencimento());

			return ResponseEntity.ok(new BoletoVO(boleto));
		}
		return ResponseEntity.notFound().build();
	}


//	@PutMapping

	@DeleteMapping("/{id}")
//	@ApiOperation(value = "Metodo para remover um boleto especifico" )
	public ResponseEntity<?> remover(@PathVariable Long id) {
		
		boletoSerivce.deleteById(id);
			return ResponseEntity.ok().build();

	}

}
