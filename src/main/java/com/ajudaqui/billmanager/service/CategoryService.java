package com.ajudaqui.billmanager.service;

import java.util.List;

import com.ajudaqui.billmanager.config.serucity.JwtUtils;
import com.ajudaqui.billmanager.entity.Category;
import com.ajudaqui.billmanager.entity.Users;
import com.ajudaqui.billmanager.exception.MsgException;
import com.ajudaqui.billmanager.repository.CategoryRepository;
import com.ajudaqui.billmanager.service.vo.CategoryVO;

import org.springframework.stereotype.*;

@Service
public class CategoryService {

  private CategoryRepository repository;
  private UsersService usersService;

  private JwtUtils jwtUtils;

  public CategoryService(CategoryRepository repository, UsersService usersService, JwtUtils jwtUtils) {
    this.repository = repository;
    this.usersService = usersService;
    this.jwtUtils = jwtUtils;
  }

  public Category create(String accessToken, String name) {
    if (name == null || name.isEmpty())
      throw new MsgException("O campo nome não pode ser vazio.");
    repository.findByName(name).ifPresent(cat -> {
      throw new MsgException("Categoria já cadastrada");
    });
    return save(new Category(name, usersService.findByAccessToken(accessToken)));
  }

  public List<Category> findAll(String accessToken) {
    return repository.findAll(jwtUtils.getAccessTokenFromJwt(accessToken));
  }

  public Category findByNameOrRegister(String name, Users users) {
    Category category = repository.findByName(name).orElseGet(() -> save(new Category(name, users)));
    if (!checkingPermission(users.getAccessToken(), category))
      throw new MsgException("Solicitação não autorizada");
    return category;
  }

  private Category findById(Long id) {
    return repository.findById(id).orElseThrow(() -> new MsgException("Categoria não localizada"));
  }

  private Category findByName(String name) {
    return repository.findByName(name).orElseThrow(() -> new MsgException("Categoria não localizada"));
  }

  public Category findByName(String accessToken, String name) {
    Category category = findByName(name);
    if (!checkingPermission(accessToken, category))
      throw new MsgException("Solicitação não autorizada");
    return category;
  }

  private boolean checkingPermission(String accessToken, Category category) {
    return jwtUtils.getAccessTokenFromJwt(accessToken).equals(category.getUsers().getAccessToken());
  }

  private Category save(Category category) {
    return repository.save(category);
  }

  public Category update(Category category) {
    return save(category);
  }

  public Category findById(String accessToken, Long id) {
    Category category = findById(id);
    if (!checkingPermission(accessToken, category))
      throw new MsgException("Solicitação não autorizada");
    return category;
  }

  public String delete(String accessToken, Long id) {
    repository.delete(findById(accessToken, id));
    return "Categoria excluida com sucesso.";
  }

  public Category update(String accessToken, Long id, CategoryVO categoryVO) {
    Category category = findById(accessToken, id);
    if (categoryVO.getName().isBlank())
      category.setName(categoryVO.getName());
    return save(category);
  }
}
