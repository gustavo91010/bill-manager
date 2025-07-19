package com.ajudaqui.billmanager.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PaymentController {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void deveRetornar201ComPagamentos() throws Exception {
    // String json = "";
 
String json = "{\"description\":\"Teste pagamento\",\"value\":100.50,\"due_date\":\"2025-07-20\"}";


    mockMvc.perform(post("/payment/repeat/3")
        .header("Authorization", "Bearer tokenDeTeste")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.data").exists());
  }
}
