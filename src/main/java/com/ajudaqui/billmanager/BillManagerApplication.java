package com.ajudaqui.billmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class BillManagerApplication {
  public static void main(String[] args) {
    SpringApplication.run(BillManagerApplication.class, args);
    System.err.println("foi!");
  }

}
