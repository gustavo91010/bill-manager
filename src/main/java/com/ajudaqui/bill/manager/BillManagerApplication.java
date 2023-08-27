package com.ajudaqui.bill.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class BillManagerApplication {

	public static void main(String[] args) {
//		http://localhost:8080/swagger-ui.html
		SpringApplication.run(BillManagerApplication.class, args);
		System.err.println("foi!!");
	}

}
