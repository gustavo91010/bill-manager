package com.ajudaqui.billmanager.service;

import java.util.List;

import com.ajudaqui.billmanager.entity.Category;
import com.ajudaqui.billmanager.entity.Users;
import com.ajudaqui.billmanager.exception.MsgException;
import com.ajudaqui.billmanager.repository.CategoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;

@Service
public class CategoryService {

  @Autowired
  private CategoryRepository repository;
  @Autowired
  private UsersService usersService;

  public Category create(String accessToken, String name) {
    if (name == null || name.isEmpty())
      throw new MsgException("O campo nome não pode ser vazio.");
    repository.findByName(name).ifPresent(cat -> {
      throw new MsgException("Categoria já cadastrada");
    });
    Users users = usersService.findByAccessToken(accessToken);
    Category category = new Category(name);
    category.setUsers(users);
    return save(category);
  }

  public List<Category> findAll(String accessToken) {
    return repository.findAll(accessToken);
  }

  private Category findById(Long id) {
    return repository.findById(id).orElseThrow(() -> new MsgException("Categoria não localizada"));
  }

  private Category findByName(String name) {
    return repository.findByName(name).orElseThrow(() -> new MsgException("Categoria não localizada"));
  }

  private Category save(Category category) {
    return repository.save(category);
  }
}
