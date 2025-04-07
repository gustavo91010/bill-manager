package com.ajudaqui.billmanager.controller;

import java.util.List;

import com.ajudaqui.billmanager.controller.from.MessageForm;
import com.ajudaqui.billmanager.controller.from.UsersForm;
import com.ajudaqui.billmanager.controller.from.UsersListFrom;
import com.ajudaqui.billmanager.entity.Users;
import com.ajudaqui.billmanager.service.UsersService;
import com.ajudaqui.billmanager.service.vo.UserUpdateVo;
import com.ajudaqui.billmanager.service.vo.UsersVO;

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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UsersController {

  @Autowired
  private UsersService usersService;
  private static final Logger LOGGER = LoggerFactory.getLogger(UsersController.class.getSimpleName());

  @PostMapping()
  public ResponseEntity<?> create(@RequestBody UsersVO userVo) {
    try {

      Users users = usersService.create(userVo);
      return ResponseEntity.ok(new UsersForm(users));
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageForm("Email já cadastrado"));
    }

  }

    @GetMapping(value = "/id/{id}")
  public ResponseEntity<?> findById(@PathVariable("id") Long id) {
    try {

      Users users = usersService.findById(id);
      return ResponseEntity.ok(new UsersForm(users));
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageForm("Usuario não encontrado"));
    }

  }

  @GetMapping(value = "/email/{email}")
  public ResponseEntity<?> findByEmail(@PathVariable("email") String email) {
    try {

      Users users = usersService.findByEmail(email);
      return ResponseEntity.ok(new UsersForm(users));
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageForm("Usuario não encontrado"));
    }

  }

  @GetMapping()
  public ResponseEntity<?> findAll() {
    try {

      List<Users> users = usersService.findAll();
      return ResponseEntity.ok(new UsersListFrom(users));
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageForm("Usuario não encontrado"));
    }

  }

  @PutMapping(value = "/{id}")
  public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody UserUpdateVo userVo) {
    try {

      Users user = usersService.update(id, userVo);
      return ResponseEntity.ok(new UsersForm(user));
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageForm("Usuario não encontrado"));
    }
  }

  @PutMapping(value = "/change/{id}")
  public ResponseEntity<?> changeDtatus(@PathVariable("id") Long id) {
    try {

      Users user = usersService.changeStatus(id);
      LOGGER.info("Usuario {} esta com o status {}.", user.getName(), user.getActive());

      return ResponseEntity.ok(new MessageForm("Usuario atualizado."));
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageForm("Usuario não encontrado"));
    }

  }

  @DeleteMapping(value = "/{id}")
  public ResponseEntity<?> delete(@PathVariable("id") Long id) {
    try {

      usersService.delete(id);
      return ResponseEntity.ok(new MessageForm("Usuario deletado."));
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageForm("Usuario não encontrado"));
    }
  }
}
