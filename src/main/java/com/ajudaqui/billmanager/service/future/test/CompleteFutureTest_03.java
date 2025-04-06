package com.ajudaqui.billmanager.service.future.test;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import com.ajudaqui.billmanager.service.future.StoreService;
import com.ajudaqui.billmanager.service.future.StoreServiceDeprecaed;

public class CompleteFutureTest_03 {
  // Se o emtodo a ser chamado não for assincrono, podemos deixar a chamada
  // assincrona.
  // Posso tambem passra o numero especifica de threads
  public void searchPriceSync(StoreServiceDeprecaed storeService) {
    long start = System.currentTimeMillis();
    ExecutorService executor = Executors.newFixedThreadPool(3);

    List<String> stores = List.of("Store 01", "Store 02", "Store 03", "Store 04", "Store 05", "Store 06");

    List<CompletableFuture<Double>> completableFutures = stores.stream()
        // to criando as trheads aqui...
        .map(s -> CompletableFuture.supplyAsync(() -> storeService.getPriceSync(s), executor))
        .collect(Collectors.toList());

    List<Double> prices = completableFutures.stream()
        .map(CompletableFuture::join)
        .collect(Collectors.toList());
    System.out.println(prices);
    long end = System.currentTimeMillis();
    System.out.println("Tempo total: " + (end - start));

    executor.shutdown();
  }
}
