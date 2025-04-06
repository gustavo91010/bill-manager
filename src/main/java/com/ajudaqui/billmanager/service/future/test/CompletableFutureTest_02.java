
package com.ajudaqui.billmanager.service.future.test;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.ajudaqui.billmanager.service.future.StoreService;

public class CompletableFutureTest_02 {
  
  // Lembrar de fazer um tratamento de cada vez para não quebrar o asyscronismo
  // das listas
  public void searchPriceSync(StoreService storeService) {
    long start = System.currentTimeMillis();

    List<String> stores = List.of("Store 01", "Store 02", "Store 03", "Store 04", "Store 05");

    List<CompletableFuture<Double>> listFuturable = stores.stream()
        .map(storeService::getPriceAsyncCompletableFuture)
        .collect(Collectors.toList());

    List<Double> listPrices = listFuturable.stream()
        .map(CompletableFuture::join).collect(Collectors.toList());

    long end = System.currentTimeMillis();
    System.out.println(listPrices);
    System.out.println("Tempo total: " + (end - start));

    StoreService.shutDown();
  }
}
