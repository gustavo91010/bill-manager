package com.ajudaqui.bill.manager.http;


import javax.validation.Valid;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ajudaqui.bill.manager.dto.EmailDto;


@FeignClient(url = "http://localhost:8081/email", name = "email-sending")
public interface EmailClient {
	
	 @PostMapping("/sending-email")
	public void sendingEmail(@RequestBody @Valid EmailDto emailDto);

}
