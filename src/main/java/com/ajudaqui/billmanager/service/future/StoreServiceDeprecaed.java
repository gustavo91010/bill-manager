package com.ajudaqui.billmanager.service.future;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class StoreServiceDeprecaed {

  // Chamando o metodo de forma sincrona
  public double getPriceSync(String store) {
    System.out.printf("Getting prices sync for store %s%n", store);
    return priceGeneragtion();
  }

  private double priceGeneragtion() {

    delay();
    // Gera numeros aleatorios em threads diferentes?
    // Gerando um valor entre 10 e 500 com o dalay de 2s
    double price = ThreadLocalRandom.current().nextInt(1, 500) * 10;
    System.out.printf("%s generating price: %.2f%n", Thread.currentThread().getName(), price);
    return price;
  }

  private void delay() {
    try {
      // int time = ThreadLocalRandom.current().nextInt(1, 5);
      int time = 2;
      TimeUnit.SECONDS.sleep(time);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
