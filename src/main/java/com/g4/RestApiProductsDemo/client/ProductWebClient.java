package com.g4.RestApiProductsDemo.client;

import com.g4.RestApiProductsDemo.dto.CreateProductDTO;
import com.g4.RestApiProductsDemo.dto.ProductDTO;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import java.util.List;

import static com.g4.RestApiProductsDemo.client.UrlConstants.BASE_URL;

public class ProductWebClient {

    private final WebClient webClient;

    public ProductWebClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(BASE_URL).build(); // Assuming local API URL
    }

    // Get all products
    public Mono<List<ProductDTO>> getAllProducts() {
        return webClient.get()
                .uri("/")
                .retrieve()
                .bodyToFlux(ProductDTO.class)
                .collectList(); // Returns a Mono containing a List of ProductDTO
    }

    // Get a product by ID
    public Mono<ProductDTO> getProductById(Long id) {
        return webClient.get()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(ProductDTO.class); // Returns a Mono containing a single ProductDTO
    }

    // Create a new product
    public Mono<ProductDTO> createProduct(CreateProductDTO productDTO) {
        return webClient.post()
                .uri("/")
                .bodyValue(productDTO)
                .retrieve()
                .bodyToMono(ProductDTO.class); // Returns a Mono containing the created ProductDTO
    }

    // Update a product by ID
    public Mono<ProductDTO> updateProduct(Long id, ProductDTO productDTO) {
        return webClient.put()
                .uri("/{id}", id)
                .bodyValue(productDTO)
                .retrieve()
                .bodyToMono(ProductDTO.class); // Returns a Mono containing the updated ProductDTO
    }

    // Delete a product by ID
    public Mono<Void> deleteProduct(Long id) {
        return webClient.delete()
                .uri("/{id}", id)
                .retrieve()
                .toBodilessEntity() // Returns a Mono<Void> as we don’t expect a body in the response
                .then(); // .then() converts the response into a Mono<Void> (completed response)
    }
}
