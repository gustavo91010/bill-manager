package com.ajudaqui.billmanager.service;

import java.util.List;

import com.ajudaqui.billmanager.entity.Users;
import com.ajudaqui.billmanager.exception.NotFoundEntityException;
import com.ajudaqui.billmanager.repository.UsersRepository;
import com.ajudaqui.billmanager.service.vo.UserUpdateVo;
import com.ajudaqui.billmanager.service.vo.UsersVO;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsersService {
  @Autowired
  private UsersRepository usersRepository;

  public Users findById(Long id) {
    return usersRepository.findById(id)
        .orElseThrow(() -> new NotFoundEntityException("Usuario não encontrado."));
  }

  public Users findByAccessToken(String accessToken) {
    return usersRepository.findByAccessToken(accessToken)
        .orElseThrow(() -> new NotFoundEntityException("hum... Usuario não encontrado."));
  }

  public boolean userExist(Long userID) {
    return usersRepository.existsById(userID);
  }

  public List<Users> findAll() {

    return usersRepository.findAll();

  }

  public Users update(Long id, UserUpdateVo userUpdateVo) {

    Users user = findById(id);

    usersRepository.save(user);
    return user;
  }

  public Users changeStats(Long id) {
    Users user = findById(id);
    user.setActive(!user.getActive());
    usersRepository.save(user);
    return user;

  }

  public void delete(Long id) {

    usersRepository.deleteById(id);

  }

  public Users registerUserBySqs(String message) {
    JsonElement jsonRegister = JsonParser.parseString(message);
    String accessToken = jsonRegister.getAsJsonObject().get("access_token").getAsString();
    Users user = usersRepository.findByAccessToken(accessToken).orElse(null);
    if (user == null) {
      user = save(new Users(accessToken));
    }
    return user;
  }

  public Users create(UsersVO userVo) {
    return new Users();
  }

  private Users save(Users users) {
    return usersRepository.save(users);
  }
}
