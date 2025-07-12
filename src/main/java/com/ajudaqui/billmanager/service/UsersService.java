package com.ajudaqui.billmanager.service;

import java.util.List;

import com.ajudaqui.billmanager.entity.Users;
import com.ajudaqui.billmanager.exception.MsgException;
import com.ajudaqui.billmanager.exception.NotFoundEntityException;
import com.ajudaqui.billmanager.repository.UsersRepository;
import com.ajudaqui.billmanager.service.vo.UserUpdateVo;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UsersService {
  @Autowired
  private UsersRepository usersRepository;

  @Value("${secretKey}")
  private String secretKey;

  public Users findById(Long id) {
    return usersRepository.findById(id)
        .orElseThrow(() -> new NotFoundEntityException("Usuario não encontrado."));
  }

  public Users findByAccessToken(String accessToken) {
    return usersRepository.findByAccessToken(accessToken)
        .orElseThrow(() -> new NotFoundEntityException("Usuario não encontrado."));
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
    return usersRepository.findByAccessToken(accessToken).orElse(save(new Users(accessToken)));
  }

  public Users create() {
    return save(new Users());
  }

  public boolean register(String authorization, String accessToken) {
    if (!secretKey.equals(authorization))
      throw new MsgException("Solicitação não autorizada");

    if (usersRepository.findByAccessToken(accessToken).isPresent())
      return true;

    Users user = usersRepository.save(new Users(accessToken));
    return user.getId() != null;
  }

  private Users save(Users users) {
    return usersRepository.save(users);
  }
}
