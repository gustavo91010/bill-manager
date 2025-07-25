package com.ajudaqui.billmanager.repository;

import java.util.List;
import java.util.Optional;

import com.ajudaqui.billmanager.entity.Category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends JpaRepository<Category, Long> {

  Optional<Category> findByName(String name);

  @Query(value = "SELECT c FROM Category c WHERE c.users.accessToken= :accessToken ORDER BY name asc", nativeQuery = false)
  List<Category> findAll(String accessToken);
}
