package com.g4.RestApiProductsDemo.client;

import com.g4.RestApiProductsDemo.dto.CreateProductDTO;
import com.g4.RestApiProductsDemo.dto.ProductDTO;
import reactor.core.publisher.Mono;

public class MainWebClientSequential {

    private static final ProductWebClient productWebClient = new ProductWebClient();

    public static Mono<Void> performOperation() {

        // Initialize DTOs for the products to be created
        CreateProductDTO newProduct = new CreateProductDTO("New Product", "New Description", 99.99, "Internal Code Sample WebClient");
        CreateProductDTO newProduct2 = new CreateProductDTO("Product2", "Desc2", 2222.22, "Internal Code 2");

        return Mono.zip(
                //Put the operations in .then() to preserve sequence
                productWebClient.createProduct(newProduct)
                .doOnNext(createdProduct ->
                        System.out.println("Created Product: \n" + createdProduct.printProductDetails())
                )
                .then(productWebClient.getAllProducts() //Is called after createProduct since its within the .then()
                        .doOnNext(products -> {
                            System.out.println("\nAll Products:");
                            products.forEach(product -> System.out.println(product.printProductDetails()));
                        })
                )
                .then(productWebClient.getProductById(1L) //Is called after getAllProducts
                        .doOnNext(product -> System.out.println("\nFetched Product: \n" + product.printProductDetails()))
                )
                .then(productWebClient.updateProduct(1L, new ProductDTO(1L, "New Name","New Desc", 9999.99)).doOnNext(product -> System.out.println("\nUpdated Product: " + product.printProductDetails()))
                ).then(productWebClient.deleteProduct(1L)
                                .doOnSuccess(bodilessEntity -> System.out.println("\nDeleted Product with ID: 1"))),

                //Run a 2nd createProduct() concurrently with the above operations
                productWebClient.createProduct(newProduct2)
                        .doOnNext(product -> System.out.println("Created Product: \n" + product.printProductDetails()))

                ).then(); // Return Mono<Void> to indicate completion
    }

    public static void main(String[] args) {

        //Has 3 callbacks
        //onNext: Used for processing data emitted by the publisher
        //onError: Used when an error occurs in the publisher Mono/Flux or onNext
        //onComplete: Used for additional logic after all data has been emitted from Mono/Flux
        performOperation().subscribe(
                null, // No value emitted
                error -> System.err.println("An error occurred: " + error.getMessage()),
                () -> System.out.println("\nWebClient Demo Completed.")  // onComplete
        );

        // Keep the main thread alive for demo purposes (to finish all requests)
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
