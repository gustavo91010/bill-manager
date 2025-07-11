package com.ajudaqui.billmanager.controller;

import com.ajudaqui.billmanager.service.UsersService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UsersController {
  @Autowired
  private UsersService usersService;

  @GetMapping("/permission") // ok
  public ResponseEntity<?> findById(@RequestHeader("Authorization") String accessToken) {

    return ResponseEntity.ok(usersService.findByAccessToken(accessToken));
  }
}
