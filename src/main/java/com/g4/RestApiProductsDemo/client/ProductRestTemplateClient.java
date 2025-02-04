package com.g4.RestApiProductsDemo.client;

import com.g4.RestApiProductsDemo.dto.CreateProductDTO;
import com.g4.RestApiProductsDemo.dto.ProductDTO;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static com.g4.RestApiProductsDemo.client.UrlConstants.BASE_URL;

public class ProductRestTemplateClient {

    private final RestTemplate restTemplate;// Update the URL if necessary

    public ProductRestTemplateClient() {
        this.restTemplate = new RestTemplate();
    }

    // Get all products
    public List<ProductDTO> getAllProducts() {
        //.exchange() returns a ResponseEntity with a body of type <T>
        //allows us to retrieve status codes and response headers
        ResponseEntity<ProductDTO[]> response = restTemplate.exchange(
                BASE_URL,
                HttpMethod.GET,
                null,
                ProductDTO[].class
        );
        return Arrays.asList(response.getBody());
    }

    // Get a product by ID
    public ProductDTO getProductById(Long id) {
        String url = BASE_URL + "/" + id;
        //More straightforward method of sending requests
        return restTemplate.getForObject(url, ProductDTO.class);
    }

    // Create a new product
    public ProductDTO createProduct(CreateProductDTO createProductDTO) {
        //More straightforward method of sending requests
        return restTemplate.postForObject(BASE_URL, createProductDTO, ProductDTO.class);
    }

    // Update an existing product
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        String url = BASE_URL + "/" + id;
        //Use HttpEntity
        HttpEntity<ProductDTO> request = new HttpEntity<>(productDTO);
        ResponseEntity<ProductDTO> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                request,
                ProductDTO.class
        );

        return response.getBody();
    }

    // Delete a product
    public void deleteProduct(Long id) {
        String url = BASE_URL + "/" + id;
        restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);
    }
}

