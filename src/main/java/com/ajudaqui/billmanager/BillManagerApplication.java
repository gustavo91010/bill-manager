package com.ajudaqui.billmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BillManagerApplication {

  public static void main(String[] args) {
    SpringApplication.run(BillManagerApplication.class, args);
    // new CompleteFutureTest_01().searchPriceSync(new StoreService());
    // new CompletableFutureTest_02().searchPriceSync(new StoreService());
    // new CompletableFutureTest_02().searchPriceSync(new StoreService());
    // new CompleteFutureTest_03().searchPriceSync(new StoreServiceDeprecaed());
  }

}
