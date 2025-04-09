package com.ajudaqui.billmanager.service;

import com.ajudaqui.billmanager.client.AuthenticationClient;
import com.ajudaqui.billmanager.client.dto.SigninDTO;
import com.ajudaqui.billmanager.client.dto.SignupDTO;
import com.ajudaqui.billmanager.client.model.SigninResponse;

import org.springframework.stereotype.Service;

@Service
public class AuthService {

  private AuthenticationClient authenticationClient;

  public SigninResponse signin(SigninDTO SigninDTO) {
    return authenticationClient.signin(SigninDTO);
  }

  public String signup(String jwtToken, SignupDTO SigninDTO) {
    return authenticationClient.signup(jwtToken, SigninDTO);
  }

  public Boolean permission(String jwtToken, String accessToken) {
    return authenticationClient.permission(jwtToken, accessToken);
  }
}
