package com.ajudaqui.controle.de.pagamentos30.repository;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.ajudaqui.controle.de.pagamentos30.entity.Boleto;
import com.ajudaqui.controle.de.pagamentos30.entity.StatusBoleto;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class BoletoRepositoryTest {

	@Autowired
	private TestEntityManager em;
	@Autowired
	private BoletoRepository repository;

	@Test
	void contextLoad2s() {
		Assert.assertFalse(false);
	}

	@Test
	void contextLoad3s() {
		Assert.assertTrue(true);
	}

	@Test
	public void deveriaBuscarBoltoPelaDescricao() {
		Boleto boleto = new Boleto();
		boleto.setDescricao("luz");
		boleto.setStatus(StatusBoleto.NAO_PAGO);
		boleto.setValor(new BigDecimal(222.22));
		boleto.setVencimento(LocalDate.now());
		em.persist(boleto);

		List<Boleto> boletosDescricao = repository.findByDescricao("luz");

		Assert.assertNotNull(boletosDescricao);
		Assert.assertEquals("luz", boletosDescricao.get(0).getDescricao());
		Assert.assertNotEquals("luiz", boletosDescricao.get(0).getDescricao());

	}

	@Test
	public void deveriaBuscarBoletosPagos() {
		// cenario:
		Boleto boleto1 = new Boleto();
		boleto1.setDescricao("luz");
		boleto1.setStatus(StatusBoleto.NAO_PAGO);
		boleto1.setValor(new BigDecimal(111.22));
//		passado
		boleto1.setVencimento(LocalDate.of(2020, 1, 1));
		em.persist(boleto1);

		Boleto boleto2 = new Boleto();
		boleto2.setDescricao("agua");
		boleto2.setStatus(StatusBoleto.PAGO);
		boleto2.setValor(new BigDecimal(222.22));
//		futuro
		boleto2.setVencimento(LocalDate.of(2222, 12, 31));
		em.persist(boleto2);

		Boleto boleto3 = new Boleto();
		boleto3.setDescricao("net");
		boleto3.setStatus(StatusBoleto.NAO_PAGO);
		boleto3.setValor(new BigDecimal(333.22));
		boleto3.setVencimento(LocalDate.now());
		em.persist(boleto3);

		Boleto boleto4 = new Boleto();
		boleto4.setDescricao("escola");
		boleto4.setStatus(StatusBoleto.PAGO);
		boleto4.setValor(new BigDecimal(444.22));
		boleto4.setVencimento(LocalDate.now());
		em.persist(boleto4);

		// execução:
		List<Boleto> pagos = repository.findBoletosPagos();

		// Verificação:
		Assert.assertNotNull(pagos);
		Assert.assertEquals(StatusBoleto.PAGO, pagos.get(0).getStatus());
		int ultimoItem = pagos.size() - 1;
		Assert.assertEquals(StatusBoleto.PAGO, pagos.get(ultimoItem).getStatus());
		Assertions.assertThat(pagos.size()).isEqualTo(2);
		Assert.assertTrue(pagos.contains(boleto2));

	}

	@Test
	public void deveChamarSomenteBoletosVencidos() {
		// cenario:
		Boleto boleto1 = new Boleto();
		boleto1.setDescricao("luz");
		boleto1.setStatus(StatusBoleto.VENCIDO);
		boleto1.setValor(new BigDecimal(111.22));
//				passado
		boleto1.setVencimento(LocalDate.of(2020, 1, 1));
		em.persist(boleto1);

		Boleto boleto2 = new Boleto();
		boleto2.setDescricao("agua");
		boleto2.setStatus(StatusBoleto.PAGO);
		boleto2.setValor(new BigDecimal(222.22));
//				futuro
		boleto2.setVencimento(LocalDate.of(2222, 12, 31));
		em.persist(boleto2);

		Boleto boleto3 = new Boleto();
		boleto3.setDescricao("net");
		boleto3.setStatus(StatusBoleto.NAO_PAGO);
		boleto3.setValor(new BigDecimal(333.22));
		boleto3.setVencimento(LocalDate.now());
		em.persist(boleto3);

		Boleto boleto4 = new Boleto();
		boleto4.setDescricao("escola");
		boleto4.setStatus(StatusBoleto.PAGO);
		boleto4.setValor(new BigDecimal(444.22));
		boleto4.setVencimento(LocalDate.now());
		em.persist(boleto4);

		// Execução:
		List<Boleto> vencidos = repository.findBoletosVencidos();

		// verificação:
		Assert.assertNotNull(vencidos);
		Assert.assertEquals(StatusBoleto.VENCIDO, vencidos.get(0).getStatus());
		// verifica o status do boleto
		Assert.assertFalse(vencidos.get(0).getStatus().equals(StatusBoleto.A_VENCER));
		// verifica se a data do boleto vem depois da data atual
		Assert.assertTrue(vencidos.get(0).getVencimento().isBefore(LocalDate.now()));
		// Assertions.assertThat(vencidos.get(0).getVencimento()).isToday();

	}

	@Test
	public void deveTrazerBoletosQueVencenNoMesAtual() {
		// cenario:
//						passado
		Boleto boleto1 = new Boleto("luz", new BigDecimal(111.22), LocalDate.of(2020, 1, 1));
		em.persist(boleto1);

//						futuro
		Boleto boleto2 = new Boleto("agua", new BigDecimal(222.22), LocalDate.of(2222, 12, 31));
		em.persist(boleto2);

		Boleto boleto3 = new Boleto("net", new BigDecimal(333.22), LocalDate.now());
		em.persist(boleto3);

		Boleto boleto4 = new Boleto("escola", new BigDecimal(444.22), LocalDate.now());
		em.persist(boleto4);

		// Execução:
		LocalDate inicioMes = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), 1);
		LocalDate fimMes = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(),
				inicioMes.lengthOfMonth());

		List<Boleto> venceEsseMes = repository.findBoletosDoMes(inicioMes, fimMes) ;

		// Verificação:

		assertNotNull(venceEsseMes);
		assertEquals(2, venceEsseMes.size());
		assertFalse(venceEsseMes.get(0).getStatus().equals(StatusBoleto.VENCIDO));
		assertFalse(venceEsseMes.get(1).getStatus().equals(StatusBoleto.VENCIDO));
		System.out.println("\nStatus: " + venceEsseMes.get(0).getStatus());
		System.out.println();
		assertTrue(venceEsseMes.get(0).getStatus().equals(StatusBoleto.VENCENDO_HOJE));

	}

}

//@RunWith(SpringRunner.class)
//@SpringBootTest
//@AutoConfigureMockMvc
//@ActiveProfiles("test")
// class BoletoControllerTest {
//	/*
//	 * na criação da classe test, eu marquiel a opção de fazer os testes com o 
//	 * junit 4 ao inves do junt jupter
//	 * ctrl + shift + M = import statico
//	 */
//
//
//	@Autowired
//	private MockMvc mockMvc;
//	
//	@MockBean
//	private BoletoRepository mocRepository;
//	
//	@Test
//	public void deveriaRetornar201CasoBoletoSejaCriado() throws Exception {
//		URI uri= new URI("/boletos");
//		
//		String json= "{\r\n"
//				+ "    \"descricao\":\"contaTest\",\r\n"
//				+ "    \"valor\":111.11,\r\n"
//				+ "    \"vencimento\":\"2023-11-22\"\r\n"
//				+ "}";
//		
//		mockMvc.perform(
//				MockMvcRequestBuilders.post(uri)
//				.content(json)
//				.contentType(MediaType.APPLICATION_JSON)
//				)
//		.andExpect(MockMvcResultMatchers.status().is(201))
//		.andExpect( MockMvcResultMatchers.jsonPath("$.descricao").value("contaTest"))
////		.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(StatusBoleto.A_VENCER))
//		.andExpect(MockMvcResultMatchers.jsonPath("$.status").value("A_VENCER"))
//		;
//	}
//	@Test
//	public void deveriaRetornarTodosOsBoletos() throws Exception {
//		URI uri= new URI("/boletos");
//		
//		String json= "{\r\n"
//				+ "    \"descricao\":\"contaTest\",\r\n"
//				+ "    \"valor\":111.11,\r\n"
//				+ "    \"vencimento\":\"2023-11-22\"\r\n"
//				+ "}";
//		mockMvc.perform(
//				get(uri)
//				.content(json)
//				.contentType(MediaType.APPLICATION_JSON)
//				)
//				.andExpect(status().isOk());
//	}
//	
//	@Test
//	public void deveriaRetornarOBoletoFiltradoPeloId() throws Exception {
////		https://www.bezkoder.com/spring-boot-webmvctest/
//		long id= 1;
//		Boleto boleto= new Boleto(id, "teste1", new BigDecimal("111.11"), LocalDate.now());
//		
//		when(mocRepository.findById(id)).thenReturn(Optional.of(boleto));
//		
//		mockMvc.perform(get("/boletos/id/{id}", id))
//		.andExpect(status().isOk())
//		.andExpect(jsonPath("$.descricao").value("teste1"))
//		.andExpect(jsonPath("$.valor").value("111.11"))
//		.andDo(MockMvcResultHandlers.print())
//		;
//   
//	}
//	@Test
//	public void DeveriaRetornarNotFoundBoleto() throws Exception {
//		long id= 1;
////		Boleto boleto= new Boleto(id, "teste1", new BigDecimal("111.11"), LocalDate.now());
//		
//		when(mocRepository.findById(id)).thenReturn(Optional.empty());
//		
//		mockMvc.perform(get("/boletos/id/{id}", 5l)).andExpect(status().is(404));
//		
//	}
//	@Test
//	public void deveriaRetornarOBoletoFiltradoPelaDescricao() throws Exception {
////		https://www.bezkoder.com/spring-boot-webmvctest/
//		long id= 1;
//		Boleto boleto= new Boleto(id, "testeDescricao", new BigDecimal("111.11"), LocalDate.now());
//		
////		when(mocRepository.findByDescricao("testeDescricao")).thenReturn(boleto);
//		
//		mockMvc.perform(get("/boletos/id/{id}", id))
//		.andExpect(status().isOk())
//		.andExpect(jsonPath("$.descricao").value("teste1"))
//		.andExpect(jsonPath("$.valor").value("111.11"))
//		.andDo(MockMvcResultHandlers.print())
//		;
//   
//	}
//
//}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
////@Test
////public void foo() {
////  MvcResult result = mockMvc.perform(get("/foo/{var}", "")).andReturn();
////  assertEquals(404, result.getResponse.getStatus());
////}
////
////@Test
////public void bar1() {
////  MvcResult result = mockMvc.perform(get("/bar/{var}", "val")).andReturn();
////  assertEquals(200, result.getResponse.getStatus());
////}
////
////@Test
////public void bar2() {
////  MvcResult result = mockMvc.perform(get("/bar/{var}", "")).andReturn();
////  assertEquals(400, result.getResponse.getStatus());
////}
////}

