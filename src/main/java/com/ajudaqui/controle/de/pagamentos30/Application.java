package com.ajudaqui.controle.de.pagamentos30;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class Application {

	public static void main(String[] args) {
//		http://localhost:8080/swagger-ui.html
		
		SpringApplication.run(Application.class, args);
		System.out.println("foi!!");
	}

}
