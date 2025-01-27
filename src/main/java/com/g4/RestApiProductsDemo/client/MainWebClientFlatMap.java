package com.g4.RestApiProductsDemo.client;

import com.g4.RestApiProductsDemo.dto.CreateProductDTO;
import com.g4.RestApiProductsDemo.dto.ProductDTO;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.CompletableFuture;

public class MainWebClientFlatMap {

    public static void main(String[] args) {

        // Initialize WebClient and ProductWebClient
        WebClient.Builder webClientBuilder = WebClient.builder();
        ProductWebClient productWebClient = new ProductWebClient(webClientBuilder);

        // Define a new product to create
        CreateProductDTO newProduct = new CreateProductDTO(
                null,
                "New Product",
                "New Description",
                99.99,
                "Internal Code Sample WebClient"
        );

        // Workflow for the product lifecycle
        CompletableFuture<Void> workflow = productWebClient.createProduct(newProduct)
                .doOnNext(product -> System.out.println("Created Product: " + product.printProductDetails()))
                .flatMap(product -> productWebClient.getProductById(product.getId())
                        .doOnNext(fetchedProduct -> System.out.println("Fetched Product: " + fetchedProduct.printProductDetails()))
                )
                .thenMany(productWebClient.getAllProducts()
                        .doOnNext(products -> {
                            System.out.println("All Products:");
                            products.forEach(ProductDTO::printProductDetails);
                        })
                )
                .then(productWebClient.deleteProduct(1L)
                        .doOnSuccess(aVoid -> System.out.println("Product with ID 1 deleted successfully."))
                )
                .then()
                .toFuture();

        // Wait for the workflow to complete
        workflow.join();
        System.out.println("WebClient Demo Completed.");
    }
}
