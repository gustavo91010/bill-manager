package com.ajudaqui.billmanager.service;

import static org.mockito.ArgumentMatchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.ajudaqui.billmanager.exception.MsgException;
import com.ajudaqui.billmanager.entity.*;
import com.ajudaqui.billmanager.repository.CategoryRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class CategoryServiceTest {

  @InjectMocks
  private CategoryService categoryService;
  @Mock
  private CategoryRepository categoryRepository;
  @Mock
  private UsersService usersService;



  @Test
  @DisplayName("Deve trazer a categoria se ela existir")
  void shouldBringUpTheCategoryIfItEXists() {
  // Ambiente
    String name = "name";
    Users user = new Users();
    user.setAccessToken("accessToken");
    Category novaCategoria = new Category(name, user);
    when(categoryRepository.findByName(name,1l)).thenReturn(Optional.of(novaCategoria));
    when(categoryRepository.save(any(Category.class))).thenReturn(novaCategoria);
    // Ececucao
    categoryService.findByNameOrRegister(name, user);
    //Verificação
    verify(categoryRepository, times(1)).findByName(name, 1l);
    verify(categoryRepository, times(0)).save(any(Category.class));
  }

  @Test
  @DisplayName("Deve registar a categoria se ela não existir")
  void mustRegisterTheCategoryIfDoesNotExist() {
  // Ambiente
    String name = "name";
    Users user = new Users();
    user.setAccessToken("accessToken");
    Category novaCategoria = new Category(name, user);
    when(categoryRepository.findByName(name, 1l)).thenReturn(Optional.empty());
    when(categoryRepository.save(any(Category.class))).thenReturn(novaCategoria);
    // Ececucao
    categoryService.findByNameOrRegister(name, user);
    //Verificação
    verify(categoryRepository, times(1)).findByName(name,1l);
    verify(categoryRepository, times(1)).save(any(Category.class));
  }

  @Test
  @DisplayName("Deve chamar o save category ao registrar")
  void mustCallSaveCaterory() {
    String name = "name";
    when(categoryRepository.findByName(name,1l)).thenReturn(Optional.empty());
    when(usersService.findByAccessToken(anyString())).thenReturn(new Users());
    categoryService.create("accessToken", name);
    verify(categoryRepository, times(1)).save(any(Category.class));
  }

  @Test
  @DisplayName("Deve lancar uma exception se nome já tver regsitrada")
  void msustThrowExceptinIsNameRegsitered() {
    String name = "name";
    when(categoryRepository.findByName(name, 1l)).thenReturn(Optional.of(new Category()));
    MsgException response = assertThrows(MsgException.class, () -> categoryService.create("accessToken", name));
    assertEquals("Categoria já cadastrada", response.getMessage());
  }

  @Test
  @DisplayName("Deve lancar uma exception se no nome estiver nulo")
  void mustThrowExceptinIfNameIsNull() {
    MsgException response = assertThrows(MsgException.class, () -> categoryService.create("accessToken", null));
    assertEquals("O campo nome não pode ser vazio.", response.getMessage());
  }

  @Test
  @DisplayName("Deve lancar uma exception se no nome estierv vazio")
  void mustThrowExceptinIfNAmeIEmpty() {
    MsgException response = assertThrows(MsgException.class, () -> categoryService.create("accessToken", ""));
    assertEquals("O campo nome não pode ser vazio.", response.getMessage());
  }
}
