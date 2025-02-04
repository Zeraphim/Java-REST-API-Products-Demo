package com.g4.RestApiProductsDemo.controller.Async;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

@Service
public class AsyncService {
//    @Async
//    public void asyncMethod() throws InterruptedException {
//        Thread.sleep(3000);
//        System.out.println("Async process completed");
//    }

    @Async
    public Future<String> asyncMethod() {
        System.out.println("Execute method asynchronously - "
                + Thread.currentThread().getName());
        try {
            Thread.sleep(5000);
            return new AsyncResult<String>("Async process completed");
        } catch (InterruptedException e) {
            //
        }

        return null;
    }
}


