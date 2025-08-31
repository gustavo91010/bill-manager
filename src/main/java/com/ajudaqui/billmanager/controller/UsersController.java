package com.ajudaqui.billmanager.controller;

import com.ajudaqui.billmanager.entity.Users;
import com.ajudaqui.billmanager.service.UsersService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UsersController {
  private UsersService usersService;

  public UsersController(UsersService usersService) {
    this.usersService = usersService;
  }

  @GetMapping("/permission") // ok
  public ResponseEntity<Users> findById(@RequestHeader("Authorization") String accessToken) {
    return ResponseEntity.ok(usersService.findByAccessToken(accessToken));
  }

  @CrossOrigin
  @PostMapping("/save/{accessToken}")
  public ResponseEntity<Boolean> register(
      @PathVariable String accessToken,
      @RequestHeader("Authorization") String authorization) {
    return ResponseEntity.ok().body(usersService.register(authorization, accessToken));
  }
}
