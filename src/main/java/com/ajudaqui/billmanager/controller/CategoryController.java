package com.ajudaqui.billmanager.controller;

import com.ajudaqui.billmanager.response.ApiCategory;
import com.ajudaqui.billmanager.response.ApiResponse;
import com.ajudaqui.billmanager.service.CategoryService;
import com.ajudaqui.billmanager.service.vo.CategoryVO;

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
    return ResponseEntity.ok(new ApiCategory(categoryService.create(accessToken, name)));
  }

  @GetMapping("/id/{id}")
  public ResponseEntity<?> findById(
      @RequestHeader("Authorization") String accessToken,
      @PathVariable Long id) {
    return ResponseEntity.ok(new ApiCategory(categoryService.findById(accessToken, id)));
  }

  @GetMapping("/name/{name}")
  public ResponseEntity<?> findByName(
      @RequestHeader("Authorization") String accessToken,
      @PathVariable String name) {
    return ResponseEntity.ok(new ApiCategory(categoryService.findByName(accessToken, name)));
  }

  @GetMapping()
  public ResponseEntity<?> findAll(
      @RequestHeader("Authorization") String accessToken) {
    return ResponseEntity.ok(categoryService.findAll(accessToken));
  }

  @PatchMapping("/{id}")
  public ResponseEntity<?> update(
      @RequestHeader("Authorization") String accessToken,
      @PathVariable Long id,
      @RequestBody CategoryVO categryVO) {
    return ResponseEntity.ok(new ApiCategory(categoryService.update(accessToken, id, categryVO)));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(
      @RequestHeader("Authorization") String accessToken,
      @PathVariable Long id) {
    return ResponseEntity.ok(new ApiResponse(categoryService.delete(accessToken, id)));
  }
}
