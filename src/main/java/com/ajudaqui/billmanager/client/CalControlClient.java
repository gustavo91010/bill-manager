package com.ajudaqui.billmanager.client;

import com.ajudaqui.billmanager.client.model.MessageCalControl;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "cal_control", url = "${url.cal_control}")
public interface CalControlClient {


  @PostMapping(value = "/template", consumes = "multipart/form-data")
  String registerTemplate(@RequestHeader("Authorization") String token, @RequestBody MessageCalControl messageCalControl);

  @PostMapping(value = "", consumes = "multipart/form-data")
  String sendMessage(@RequestHeader("Authorization") String token, @RequestBody MessageCalControl messageCalControl);

}
