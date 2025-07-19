package com.ajudaqui.billmanager.client;

import com.ajudaqui.billmanager.client.dto.SigninDTO;
import com.ajudaqui.billmanager.client.dto.SignupDTO;
import com.ajudaqui.billmanager.client.model.SigninResponse;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "authenticador", url = "${url.authenticador}")
public interface AuthenticationClient {

  @PostMapping(value = "/auth/signin", consumes = "multipart/form-data")
  SigninResponse signin(@RequestBody SigninDTO SigninDto);


  @PostMapping(value = "/auth/signup", consumes = "multipart/form-data")
  String signup(@RequestHeader("Authorization") String jwtToken, @RequestBody SignupDTO SigninDto);

  @PostMapping(value = "/auth/permission/{accessToken}", consumes = "application/json")
  Boolean permission(@RequestHeader("Authorization") String jwtToken, @PathVariable String accessToken);
}
