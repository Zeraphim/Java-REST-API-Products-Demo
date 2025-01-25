package com.g4.RestApiProductsDemo.client;

import com.g4.RestApiProductsDemo.dto.CreateProductDTO;
import com.g4.RestApiProductsDemo.dto.ProductDTO;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.CompletableFuture;

public class MainWebClient {

    public static void main(String[] args) {

        // Create a WebClient instance with the base URL
        WebClient.Builder webClientBuilder = WebClient.builder();
        ProductWebClient productWebClient = new ProductWebClient(webClientBuilder);

        // Example: Get all products (non-blocking)
        CompletableFuture<Void> fetchAllProducts = productWebClient.getAllProducts()
                .doOnNext(product -> System.out.println("Product: " + product))
                .then()
                .toFuture();

        // Example: Get product by ID (non-blocking)
        CompletableFuture<Void> fetchProductById = productWebClient.getProductById(1L)
                .doOnNext(product -> System.out.println("Fetched Product: " + product))
                .then()
                .toFuture();

        // Example: Create a new product (non-blocking)
        CreateProductDTO newProduct = new CreateProductDTO(null, "New Product", "New Description", 99.99, "Internal Code Sample WebClient");
        CompletableFuture<Void> createProduct = productWebClient.createProduct(newProduct)
                .doOnNext(product -> System.out.println("Created Product: " + product))
                .then()
                .toFuture();

        // Wait for all async tasks to complete
        CompletableFuture.allOf(fetchAllProducts, fetchProductById, createProduct)
                .join();

        System.out.println("WebClient Demo Completed.");
    }
}
