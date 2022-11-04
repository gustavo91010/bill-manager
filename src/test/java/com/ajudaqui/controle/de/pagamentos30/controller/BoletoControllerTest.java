package com.ajudaqui.controle.de.pagamentos30.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

//import org.junit.Test;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.ajudaqui.controle.de.pagamentos30.repository.BoletoRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BoletoControllerTest {
	/*
	 * na criação da classe test, eu marquiel a opção de fazer os testes com o 
	 * junit 4 ao inves do junt jupter
	 * ctrl + shift + M = import statico
	 */


	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private BoletoRepository mocRepository;
	
	@Test
	public void deveriaRetornar201CasoBoletoSejaCriado() throws Exception {
		URI uri= new URI("/boletos");
		
		String json= "{\r\n"
				+ "    \"descricao\":\"contaTest\",\r\n"
				+ "    \"valor\":111.11,\r\n"
				+ "    \"vencimento\":\"2023-11-22\"\r\n"
				+ "}";
		
		mockMvc.perform(
				MockMvcRequestBuilders.post(uri)
				.content(json)
				.contentType(MediaType.APPLICATION_JSON)
				)
		.andExpect(MockMvcResultMatchers.status().is(201))
		.andExpect( MockMvcResultMatchers.jsonPath("$.descricao").value("contaTest"))
//		.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(StatusBoleto.A_VENCER))
		.andExpect(MockMvcResultMatchers.jsonPath("$.status").value("A_VENCER"))
		;
	}
	@Test
	public void deveriaRetornarTodosOsBoletos() throws Exception {
		URI uri= new URI("/boletos");
		
		String json= "{\r\n"
				+ "    \"descricao\":\"contaTest\",\r\n"
				+ "    \"valor\":111.11,\r\n"
				+ "    \"vencimento\":\"2023-11-22\"\r\n"
				+ "}";
		mockMvc.perform(
				get(uri)
				.content(json)
				.contentType(MediaType.APPLICATION_JSON)
				)
				.andExpect(status().isOk());
	}
	
	@Test
	public void deveriaRetornarOBoletoFiltradoPeloId() throws Exception {
//		https://www.bezkoder.com/spring-boot-webmvctest/
		long id= 1;
		Boleto boleto= new Boleto(id, "teste1", new BigDecimal("111.11"), LocalDate.now());
		
		Mockito.when(mocRepository.findById(id)).thenReturn(Optional.of(boleto));
		
		mockMvc.perform(MockMvcRequestBuilders.get("/boletos/id/{id}", id))
		 	 .andExpect(MockMvcResultMatchers.status().isOk())
		 	 .andExpect(MockMvcResultMatchers.jsonPath("$.descricao").value("teste1"))
		 	 .andExpect(MockMvcResultMatchers.jsonPath("$.valor").value("111.11"))
		 	 .andDo(MockMvcResultHandlers.print())
		;
 
	}
	@Test
	public void DeveriaRetornarNotFoundBoleto() throws Exception {
		long id= 1;
//		Boleto boleto= new Boleto(id, "teste1", new BigDecimal("111.11"), LocalDate.now());
		
		when(mocRepository.findById(id)).thenReturn(Optional.empty());
		
		mockMvc.perform(get("/boletos/id/{id}", 5l)).andExpect(status().is(404));
		
	}
	@Test
	public void deveriaRetornarOBoletoFiltradoPelaDescricao() throws Exception {
//		https://www.bezkoder.com/spring-boot-webmvctest/
		long id= 1;
		Boleto boleto= new Boleto(id, "testeDescricao", new BigDecimal("222.11"), LocalDate.now());
		List<Boleto> boletos= new ArrayList<>();
		boletos.add(boleto);
		
		when(mocRepository.findByDescricao("testeDescricao")).thenReturn(boletos);
		
		mockMvc.perform(get("/boletos/descricao/{descricao}", "testeDescricao"))
		.andExpect(status().isOk())
		.andDo(MockMvcResultHandlers.print())
		;
 
	}
	@Test
	public void deveriaRetornar404SeListaEstiverVazia() throws Exception {
//		https://www.bezkoder.com/spring-boot-webmvctest/
		
		when(mocRepository.findByDescricao("testeDescricao")).thenReturn(Collections.emptyList());
		
		mockMvc.perform(get("/boletos/descricao/{descricao}", "testeDescricao"))
		.andExpect(status().isNotFound())
		.andDo(MockMvcResultHandlers.print())
		;
		
	}
	
	@Test
	public void deveriaRetornarSomenteBoletosPagos() throws Exception {
		Boleto boleto= new Boleto( "pago", new BigDecimal("222.11"), LocalDate.now());
		boleto.setStatus(StatusBoleto.PAGO);
		List<Boleto> boletos= new ArrayList<>();
		boletos.add(boleto);
		
		when(mocRepository.findBoletosPagos()).thenReturn(boletos);
		mockMvc.perform(
				get("/boletos/pago"))
		.andExpect(jsonPath("$[0].status").value("PAGO"))
		.andExpect(status().isOk())
		.andDo(MockMvcResultHandlers.print());
		
		
	}

}























//@Test
//public void foo() {
//MvcResult result = mockMvc.perform(get("/foo/{var}", "")).andReturn();
//assertEquals(404, result.getResponse.getStatus());
//}
//
//@Test
//public void bar1() {
//MvcResult result = mockMvc.perform(get("/bar/{var}", "val")).andReturn();
//assertEquals(200, result.getResponse.getStatus());
//}
//
//@Test
//public void bar2() {
//MvcResult result = mockMvc.perform(get("/bar/{var}", "")).andReturn();
//assertEquals(400, result.getResponse.getStatus());
//}
//}
