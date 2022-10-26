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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.ajudaqui.controle.de.pagamentos30.entity.Boleto;
import com.ajudaqui.controle.de.pagamentos30.entity.StatusBoleto;
import com.ajudaqui.controle.de.pagamentos30.entity.ValidarStatus;
import com.ajudaqui.controle.de.pagamentos30.entity.Vo.BoletoVO;
import com.ajudaqui.controle.de.pagamentos30.from.BoletoFrom;
import com.ajudaqui.controle.de.pagamentos30.repository.BoletoRepository;
import com.ajudaqui.controle.de.pagamentos30.specification.BoletoSpecification;

import io.swagger.annotations.ApiOperation;



@RestController
@RequestMapping("/boletos")
public class BoletoController {

	@Autowired
	private BoletoRepository repository;

	@PostMapping
	@ApiOperation(value = "Salva um novo boleto no banco de dados" )
	public ResponseEntity<BoletoVO> cadastrar(@RequestBody BoletoFrom from, UriComponentsBuilder uriBuilder) {
		Boleto boleto = new Boleto(from.getDescricao(), from.getValor(), from.getVencimento());
		repository.save(boleto);

		URI uri = uriBuilder.path("/boletos/{id}").buildAndExpand(boleto.getId()).toUri();
		return ResponseEntity.created(uri).body(new BoletoVO(boleto));

	}

	@GetMapping
	@ApiOperation(value = "Chama todos os boletos registrados." )
	public List<Boleto> consultar() {
		List<Boleto> boletos = repository.findAll();
		
		atualizarStatus(boletos);
		

		return boletos;

	}

	@GetMapping(value = "/id/{id}")
	@ApiOperation(value = "Chama os boletos filtrando pelo Id" )
	public BoletoVO consultarPorId(@PathVariable("id") Long id) {
		Boleto boleto = repository.findById(id).orElseThrow();

		return new BoletoVO(boleto);

	}
	@GetMapping(value = "/descricao/{descricao}")
	@ApiOperation(value = "Chama os boletos filtrando pela Descrição" )
	public List<BoletoVO> consultarPorDescricao(@PathVariable("descricao") String descricao) {
		List<Boleto> boletos = repository.findByDescricao(descricao);
		List<BoletoVO> boletosVO= new ArrayList<>();
		boletos.forEach(b->{
			boletosVO.add(new BoletoVO(b));
		});
		
		return boletosVO;
		
	}
	@GetMapping(value= "/pago")
	@ApiOperation(value = "Mostra os boletos que ja foram pago " )
	public List<BoletoVO> mostrarBoletosPagos() {
		List<Boleto> b= repository.findBoletosPagos();;
	List<BoletoVO> boletos= new ArrayList<>();
	b.forEach(bo->{
		boletos.add(new BoletoVO(bo));
	});
	return boletos;
	}
	
	@GetMapping(value= "/vencido")
	@ApiOperation(value = "Chama os boletos que estao vencidos" )
	public List<BoletoVO> mostrarBoletosVencidos() {
		List<Boleto> b= repository.findBoletosVencidos();
		List<BoletoVO> boletos= new ArrayList<>();
		b.forEach(bo->{
			boletos.add(new BoletoVO(bo));
		});
		return boletos;
	}
	
	@GetMapping(value="/mes")
	@ApiOperation(value = "Chama os boletos a serem pagos no mes atual" )
	public List<BoletoVO> mostrarBoletosMes() {
		
		LocalDate inicioMes= LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), 1);
		LocalDate fimMes= LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), inicioMes.lengthOfMonth());
		List<Boleto> boletos= repository.findBoletosVencimentoProximo(inicioMes, fimMes);
		
		
		List<BoletoVO> boletosVO= new ArrayList<>();
		
		boletos.forEach(b->{
			boletosVO.add(new BoletoVO(b));
		});
//		List<Boleto> boletos= repository.findAll();
//		System.out.println("Quantos boletos antes : "+boletos.size());
//		boletos.forEach(b->{
//			System.out.println("dentro do laço: "+b.getVencimento());
//			if(b.getVencimento().compareTo(inicioMes)>=1 && b.getVencimento().compareTo(fimMes)<0) {
//				System.out.println("dentro do if: "+b.getVencimento());
//				boletosVO.add(new BoletoVO(b));
//			}
//		});
//
//		System.out.println("Quantos boletos depois: "+boletosVO.size());
		return boletosVO;
	}
	@GetMapping(value="/mes/{mes}")
	@ApiOperation(value = "Chama os boletos que serao pagos no mes especificado pelo usuario (de 1  a12)" )
	public List<BoletoVO> mostrarBoletosMes(@PathVariable int mes) {
		
		LocalDate inicioMes= LocalDate.of(LocalDate.now().getYear(), mes, 1);
		LocalDate fimMes= LocalDate.of(LocalDate.now().getYear(), mes, inicioMes.lengthOfMonth());
		List<Boleto> boletos= repository.findBoletosVencimentoProximo(inicioMes, fimMes);
		
		
		List<BoletoVO> boletosVO= new ArrayList<>();
		
		boletos.forEach(b->{
			boletosVO.add(new BoletoVO(b));
		});
		return boletosVO;
	}
	@GetMapping("/dinamico")
	@ApiOperation(value = "Faz uma consulta com dados variados sobre o boleto" )
	List<BoletoVO> findByBuscaDinamica(@RequestBody BoletoFrom boletoFrom){
		
		Specification<Boleto> spefications = Specification.where(
						BoletoSpecification.descricao(boletoFrom.getDescricao())
//					.or(BoletoSpecification.status(boletoFrom.getStatus() ) )
							
//					.or(BoletoSpecification.vencimento(boletoFrom.getVencimento()))
				 );
		
		List<Boleto> boletos= repository.findAll(spefications);
		
		List<BoletoVO> boletosVO=new  ArrayList<>();
		boletos.forEach(b->{
			boletosVO.add(new BoletoVO(b));
		});
		
		return boletosVO;
		
	};

	@PutMapping("/{id}")
	@ApiOperation(value = "Atualiza qualquer dado de um boleto" )
	public ResponseEntity<BoletoVO> atualizar(@PathVariable Long id, @RequestBody BoletoFrom from) {
		
		Optional<Boleto> boleto = repository.findById(id);
		if (boleto.isPresent()) {
			boleto.get().setDescricao(from.getDescricao());
			boleto.get().setValor(from.getValor());
			boleto.get().setVencimento(from.getVencimento());
			
			return ResponseEntity.ok(new BoletoVO(boleto.get()));
		}
		return ResponseEntity.notFound().build();
	}
	

	
	
	@PutMapping("/pagar/{id}")
	@ApiOperation(value = "Metodo para informar o pagamento de um boleto" )
	public ResponseEntity<BoletoVO> pagarBoleto(@PathVariable Long id) {
		Optional<Boleto> boleto= repository.findById(id);
		if(boleto.isPresent()) {
			
		boleto.get().setStatus(StatusBoleto.PAGO);
		repository.save(boleto.get());
		return ResponseEntity.ok(new BoletoVO(boleto.get()));
		}
		return ResponseEntity.notFound().build();
	}
	
//	@PutMapping
	public List<Boleto> atualizarStatus(List<Boleto> boletos) {
//		ValidarStatus.statusAtualizado(StatusBoleto status, LocalDate vencimento);
//		return ValidarStatus.statusAtualizado(boleto, repository);
		
		boletos.forEach(b->{
			ValidarStatus.statusAtualizado(b, repository);

		});
		return boletos;
//		return
		
	}
	
	
	
	@DeleteMapping("/{id}")
	@ApiOperation(value = "Metodo para remover um boleto especifico" )
	public ResponseEntity<?> remover(@PathVariable Long id) {
		Optional<Boleto> boleto= repository.findById(id);
		if(boleto.isPresent()) {
			repository.deleteById(id);
			return ResponseEntity.ok().build();
		}
		
		return ResponseEntity.notFound().build(); 
		
	}
	
	
	
	

}
