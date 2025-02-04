package com.g4.RestApiProductsDemo.client;

import com.g4.RestApiProductsDemo.dto.CreateProductDTO;
import com.g4.RestApiProductsDemo.dto.ProductDTO;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class MainWebClient {

    private static final ProductWebClient productWebClient = new ProductWebClient();

    //We return Mono<Void> as an indicator that this method performs asynchronous tasks
    //The type is Void since we only want to print the responses (which we will do inside this function)
    //we don't need to pass values to subscribers returned for further processing
    public static Mono<Void> performOperations() {
        // Initialize DTO for new product
        CreateProductDTO newProduct = new CreateProductDTO("New Product", "New Description", 99.99, "Internal Code Sample WebClient");

        // Perform all the operations in parallel using Mono.zip()
        // For Flux, there is a Flux.zip()
        return Mono.zip(

                //Create a new product
                //doOnNext() allows us to write logic for the Mono/Flux result returned to us by the WebClient
                productWebClient.createProduct(newProduct)
                        .doOnNext(product -> System.out.println("Created Product: \n" + product.printProductDetails()))
                        .then(), // .then() tells the stream to continue with the next steps
                                 // This allows us to disregard the previous data we used, since we only
                                 // needed it for printing the created product's details

                //Get all products
                productWebClient.getAllProducts()
                        .doOnNext(products -> {
                            System.out.println("\nAll Products:");
                            products.forEach(ProductDTO::printProductDetails);
                        })
                        .then(),

                //Get product by ID
                productWebClient.getProductById(999L)
                        .doOnNext(product -> System.out.println("\nFetched Product: \n" + product.printProductDetails()))
                        .then(),

                //Update product by ID
                productWebClient.updateProduct(1L, new ProductDTO(1L, "New Name", "New Desc", 999.99))
                        .doOnNext(product -> System.out.println("\nUpdated Product: \n" + product.printProductDetails()))
                        .then(),

                //Delete product by ID
                productWebClient.deleteProduct(1L)
                        .doOnSuccess(aVoid -> System.out.println("\nProduct with ID 1 deleted successfully."))
                        .then()
        ).then();  // Flatten the results to Mono<Void>
    }

    public static void main(String[] args) {

        // Subscribe to the Mono to initiate the operations
        //Has 3 callbacks
        //onNext: Used for processing data emitted by the publisher
        //onError: Used when an error occurs in the publisher Mono/Flux or onNext
        //onComplete: Used for additional logic after all data has been emitted from Mono/Flux
        performOperations().subscribe(
                null,  // onNext (no value emitted)
                error -> System.err.println("An error occurred: " + error.getMessage()), //onError
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
