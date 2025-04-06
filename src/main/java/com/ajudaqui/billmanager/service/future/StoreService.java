package com.ajudaqui.billmanager.service.future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class StoreService {
  private static ExecutorService executor = Executors.newCachedThreadPool();

  // Chamando o metodo de forma sincrona
  public double getPriceSync(String store) {
    System.out.printf("Getting prices sync for store %s%n", store);
    return priceGeneragtion();
  }

  public CompletableFuture<Double> getPriceAsyncCompletableFuture(String store) {
    System.out.printf("Getting prices sync for store %s%n", store);

    long start = System.currentTimeMillis();

    CompletableFuture<Double> lalala = CompletableFuture.supplyAsync(this::priceGeneragtion);

    long end = System.currentTimeMillis();
    System.out.println("Tempo de processamento: " + (end - start));
    return lalala;
  }

  // Chamando o metodo de forma assincrona
  public Future<Double> getPriceAsyncFuture(String store) {
    System.out.printf("Getting prices sync for store %s%n", store);

    // Colocar o metod dentro do executor...
    return executor.submit(this::priceGeneragtion);
  }

  public static void shutDown() {
    executor.shutdown();
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
      int time = ThreadLocalRandom.current().nextInt(1, 5);
      // int time = 5;
      TimeUnit.SECONDS.sleep(time);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
