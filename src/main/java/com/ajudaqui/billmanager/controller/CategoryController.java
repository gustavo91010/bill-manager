package com.ajudaqui.billmanager.controller;

import com.ajudaqui.billmanager.service.CategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category")
public class CategoryController {

  @Autowired
  private CategoryService categoryService;

  @PostMapping("/{name}")
  public ResponseEntity<?> create(
      @RequestHeader("Authorization") String accessToken,
      @PathVariable("name") String name) {
    return ResponseEntity.ok(categoryService.create(accessToken, name));
  }

  @GetMapping()
  public ResponseEntity<?> findAll(
      @RequestHeader("Authorization") String accessToken) {
    return ResponseEntity.ok(categoryService.findAll(accessToken));
  }

}
