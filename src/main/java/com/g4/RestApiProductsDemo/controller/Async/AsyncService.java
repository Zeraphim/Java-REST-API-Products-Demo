package com.g4.RestApiProductsDemo.controller.Async;

import com.g4.RestApiProductsDemo.dto.ProductDTO;
import com.g4.RestApiProductsDemo.entity.Product;
import com.g4.RestApiProductsDemo.exception.ProductNotFoundException;
import com.g4.RestApiProductsDemo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
public class AsyncService {
    @Async
    public void asyncMethod() {
        System.out.println("Async process completed");
    }
}


