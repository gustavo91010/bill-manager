package com.ajudaqui.billmanager.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ajudaqui.billmanager.entity.Users;
import com.ajudaqui.billmanager.service.UsersService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UsersController.class)
public class UsersControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UsersService usersService;

  @Test
  @DisplayName("ha, to com sono...")
  void mustFindUserById() throws Exception {
    Users userMock = new Users();
    userMock.setId(1L);
    userMock.setName("Bob");

    when(usersService.findById(1L)).thenReturn(userMock);

    mockMvc.perform(get("/users/id/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.user.id").value(1))
        .andExpect(jsonPath("$.user.name").value("Bob"));
  }
}
