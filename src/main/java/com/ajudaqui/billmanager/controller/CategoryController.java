package com.ajudaqui.billmanager.controller;

import java.util.List;

import com.ajudaqui.billmanager.entity.Category;
import com.ajudaqui.billmanager.response.ApiCategory;
import com.ajudaqui.billmanager.response.ApiResponse;
import com.ajudaqui.billmanager.service.CategoryService;
import com.ajudaqui.billmanager.service.vo.CategoryVO;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/category")
public class CategoryController {

  private CategoryService categoryService;

  public CategoryController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @PostMapping("/{name}")
  public ResponseEntity<ApiCategory> create(
      @RequestHeader("Authorization") String accessToken,
      @PathVariable("name") String name) {
    return ResponseEntity.ok(new ApiCategory(categoryService.create(accessToken, name)));
  }

  @GetMapping("/id/{id}")
  public ResponseEntity<ApiCategory> findById(
      @RequestHeader("Authorization") String accessToken,
      @PathVariable Long id) {
    return ResponseEntity.ok(new ApiCategory(categoryService.findById(accessToken, id)));
  }

  @GetMapping("/name/{name}")
  public ResponseEntity<ApiCategory> findByName(
      @RequestHeader("Authorization") String accessToken,
      @PathVariable String name) {
    return ResponseEntity.ok(new ApiCategory(categoryService.findByName(accessToken, name)));
  }

  @GetMapping()
  public ResponseEntity<List<Category>> findAll(
      @RequestHeader("Authorization") String accessToken) {
    return ResponseEntity.ok(categoryService.findAll(accessToken));
  }

  @PatchMapping("/{id}")
  public ResponseEntity<ApiCategory> update(
      @RequestHeader("Authorization") String accessToken,
      @PathVariable Long id,
      @RequestBody CategoryVO categryVO) {
    return ResponseEntity.ok(new ApiCategory(categoryService.update(accessToken, id, categryVO)));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse> delete(
      @RequestHeader("Authorization") String accessToken,
      @PathVariable Long id) {
    return ResponseEntity.ok(new ApiResponse(categoryService.delete(accessToken, id)));
  }
}
