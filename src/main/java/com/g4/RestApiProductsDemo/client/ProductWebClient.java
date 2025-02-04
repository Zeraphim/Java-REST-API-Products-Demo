package com.g4.RestApiProductsDemo.client;

import com.g4.RestApiProductsDemo.dto.CreateProductDTO;
import com.g4.RestApiProductsDemo.dto.ProductDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import java.util.List;

import static com.g4.RestApiProductsDemo.client.UrlConstants.BASE_URL;

public class ProductWebClient {

    private final WebClient webClient ;

    public ProductWebClient() {
        this.webClient = WebClient.create(BASE_URL); //Create a WebClient object and set the BASE_URL as base endpoint
    }

    // Create a new product
    public Mono<ProductDTO> createProduct(CreateProductDTO createProductDTO) {
        //WebClient has built-in methods for GET, PUT, POST, DELETE
        return webClient.post()
                .uri("") //append URI to the base URL
                .bodyValue(createProductDTO) //Include body to request if needed
                .retrieve() //Initiate request and handle the response
                .bodyToMono(ProductDTO.class); // From response, returns the body as Mono containing the created ProductDTO
    }

    // Get a product by ID
    public Mono<ProductDTO> getProductById(Long id) {
        //WebClient has built-in methods for GET, PUT, POST, DELETE
        return webClient.get()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(ProductDTO.class); // Returns a Mono containing a single ProductDTO
    }

    // Get all products
    public Mono<List<ProductDTO>> getAllProducts() {
        //WebClient has built-in methods for GET, PUT, POST, DELETE
        return webClient.get()
                .uri("")
                .retrieve()
                .bodyToFlux(ProductDTO.class)
                .collectList(); // Returns a Mono containing a List of ProductDTO
    }

    // Update a product by ID
    public Mono<ProductDTO> updateProduct(Long id, ProductDTO productDTO) {
        //WebClient has built-in methods for GET, PUT, POST, DELETE
        return webClient.put()
                .uri("/{id}", id)
                .bodyValue(productDTO)
                .retrieve()
                .bodyToMono(ProductDTO.class); // Returns a Mono containing the updated ProductDTO
    }

    // Delete a product by ID
    public Mono<Void> deleteProduct(Long id) {
        //WebClient has built-in methods for GET, PUT, POST, DELETE
        return webClient.delete()
                .uri("/{id}", id)
                .retrieve()
                .toBodilessEntity() // Returns a Void as we don’t expect a body in the response
                .then(); // .then() converts the response into a Mono<Void> (completed response)
    }
}
