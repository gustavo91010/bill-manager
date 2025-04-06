package com.ajudaqui.billmanager.service.future.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import com.ajudaqui.billmanager.service.future.StoreService;

public class CompleteFutureTest_01 {

  public void searchPriceSync(StoreService storeService) {
    long start = System.currentTimeMillis();

    // crio uma lista para guardar as threads
    List<Future<Double>> futures = new ArrayList<>();
    List<CompletableFuture<Double>> completableFutures = new ArrayList<>();

    for (int i = 0; i < 9; i++) {

      // Chamando o sincrono...
      // System.out.println(storeService.getPriceSync("Store " + (i + 1)));
      // long end = System.currentTimeMillis();
      // System.out.println("tempo passado: " + (end - start));

      // executa o processamento da busca pelo valor em thears separada.
      // futures.add(storeService.getPriceAsyncFuture("Store " + (i + 1)));
      completableFutures.add(storeService.getPriceAsyncCompletableFuture("Store " + (i + 1)));
    }
    CompletableFuture<Void> all = CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0]));

    // aqui, eu garanto que todos as threads terminarem
    all.join();

    // for (Future<Double> future : futures) {
    for (CompletableFuture<Double> future : completableFutures) {
      System.out.println(future.join());

      // try {
      // // Da pra por um time out aqui...
      // // System.out.println(future.get(3, TimeUnit.SECONDS));
      // // System.out.println(future.get());
      // } catch (InterruptedException | ExecutionException e) {
      // // TODO Auto-generated catch block
      // e.printStackTrace();
      // }

      long end = System.currentTimeMillis();
      System.out.println("tempo passado: " + (end - start));

    }

    StoreService.shutDown();
  }
}
