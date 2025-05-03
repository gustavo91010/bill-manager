package com.ajudaqui.billmanager.controller;

import java.util.List;

import com.ajudaqui.billmanager.entity.Users;
import com.ajudaqui.billmanager.service.UsersService;
import com.ajudaqui.billmanager.service.vo.UserUpdateVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UsersController {

  @Autowired
  private UsersService usersService;

  @GetMapping(value = "/id/{id}")
  public ResponseEntity<?> findById(@PathVariable("id") Long id) {
    try {

      Users users = usersService.findById(id);
      return ResponseEntity.ok(users);
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("OUsuario não encontrado");
    }
  }

  @GetMapping()
  public ResponseEntity<?> findAll() {
    try {

      List<Users> users = usersService.findAll();
      return ResponseEntity.ok(users);
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("OUsuario não encontrado");
    }

  }

  @PutMapping(value = "/{id}")
  public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody UserUpdateVo userVo) {
    try {

      usersService.update(id, userVo);
      return ResponseEntity.ok("Usuario atualizado.");
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Usuario não encontrado");
    }
  }

  @DeleteMapping(value = "/{id}")
  public ResponseEntity<?> delete(@PathVariable("id") Long id) {
    try {

      usersService.delete(id);
      return ResponseEntity.ok("Usuario deletado.");
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Usuario não encontrado");
    }

  }

}
