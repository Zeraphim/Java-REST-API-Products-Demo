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

        // Create a new product (non-blocking)
        CreateProductDTO newProduct = new CreateProductDTO("New Product", "New Description", 99.99, "Internal Code Sample WebClient");
        CompletableFuture<Void> createProduct = productWebClient.createProduct(newProduct)
                .doOnNext(product -> System.out.println("Created Product: \n" + product.printProductDetails()))
                .then()
                .toFuture();

        // Get all products (non-blocking)
        CompletableFuture<Void> fetchAllProducts = productWebClient.getAllProducts()
                .doOnNext(products -> {
                    System.out.println("All products: \n");
                    products.forEach(ProductDTO::printProductDetails);})
                .then()
                .toFuture();

        // Get product by ID (non-blocking)
        CompletableFuture<Void> fetchProductById = productWebClient.getProductById(1L)
                .doOnNext(product -> System.out.println("Fetched Product: \n" + product.printProductDetails()))
                .then()
                .toFuture();

        // Delete a product by ID (non-blocking)
        CompletableFuture<Void> deleteProductById = productWebClient.deleteProduct(1L)
                .doOnSuccess(aVoid -> System.out.println("Product with ID 1 deleted successfully."))
                .then()
                .toFuture();

        // Wait for all async tasks to complete
        CompletableFuture.allOf(fetchAllProducts, fetchProductById, createProduct, deleteProductById)
                .join();

        System.out.println("WebClient Demo Completed.");
    }
}
