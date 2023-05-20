package com.ajudaqui.controle.de.pagamentos30.http;


import javax.validation.Valid;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ajudaqui.controle.de.pagamentos30.dto.EmailDto;


@FeignClient(url = "http://localhost:8081/email", name = "email-sending")
public interface EmailClient {
	
	 @PostMapping("/sending-email")
	public void sendingEmail(@RequestBody @Valid EmailDto emailDto);

}
