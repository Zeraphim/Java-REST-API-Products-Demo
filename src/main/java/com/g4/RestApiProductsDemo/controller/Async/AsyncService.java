package com.g4.RestApiProductsDemo.controller.Async;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncService {
    @Async
    public static void asyncMethod() throws InterruptedException {
        Thread.sleep(3000);
        System.out.println("Async process completed");
    }
}
