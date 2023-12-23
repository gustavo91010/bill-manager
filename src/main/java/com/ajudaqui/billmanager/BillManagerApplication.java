package com.ajudaqui.billmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BillManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BillManagerApplication.class, args);
		System.err.println("foi!");
	}

}
