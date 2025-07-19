package com.ajudaqui.billmanager.service;

import com.ajudaqui.billmanager.client.AuthenticationClient;
import com.ajudaqui.billmanager.client.dto.SigninDTO;
import com.ajudaqui.billmanager.client.dto.SignupDTO;
import com.ajudaqui.billmanager.client.model.SigninResponse;

import org.springframework.stereotype.Service;

@Service
public class AuthService {

  private AuthenticationClient authenticationClient;

  public SigninResponse signin(SigninDTO SigninDto) {
    return authenticationClient.signin(SigninDto);
  }

  public String signup(String jwtToken, SignupDTO SigninDto) {
    return authenticationClient.signup(jwtToken, SigninDto);
  }

  public Boolean permission(String jwtToken, String accessToken) {
    return authenticationClient.permission(jwtToken, accessToken);
  }
}
