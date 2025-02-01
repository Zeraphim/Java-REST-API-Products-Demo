package com.g4.RestApiProductsDemo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.g4.RestApiProductsDemo.dto.CreateProductDTO;
import com.g4.RestApiProductsDemo.exception.InvalidProductException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.g4.RestApiProductsDemo.dto.ProductDTO;
import com.g4.RestApiProductsDemo.exception.ResourceNotFoundException;
import com.g4.RestApiProductsDemo.service.ProductService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v4/product")
public class ProductControllerV4 {

    @Autowired
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    // Get all products
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllProduct() {
        List<ProductDTO> products = productService.getAllProduct();
        int statusCode = determineStatusCode(products); // Method to determine status code
        Map<String, Object> response = createResponse(statusCode, products);
        return ResponseEntity.status(statusCode).body(response);
    }

    // Get a product by ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        ProductDTO product = productService.getProductById(id);
        if (product == null) {
            throw new ResourceNotFoundException("Product not found with id " + id);
        }
        return ResponseEntity.ok(product);
    }

    // Create a new product
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ObjectNode productNode) {
        validateProductNode(productNode);
        CreateProductDTO createProductDTO = objectMapper.convertValue(productNode, CreateProductDTO.class);
        ProductDTO createdProduct = productService.createProduct(createProductDTO);
        return ResponseEntity.ok(createdProduct);
    }

    // Update a product by ID
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ObjectNode productNode) {
        validateProductNode(productNode);
        ProductDTO productDTO = objectMapper.convertValue(productNode, ProductDTO.class);
        ProductDTO updatedProduct = productService.updateProduct(id, productDTO);
        return ResponseEntity.ok(updatedProduct);
    }

    // Delete a product by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteProduct(@PathVariable Long id) {
        Map<String, Object> response = productService.deleteProduct(id);
        return ResponseEntity.ok(response);
    }

    /////////////////// Authenticated Endpoints START ///////////////////
    @GetMapping("/secured")
    public ResponseEntity<Map<String, Object>> getAllProductSecured() {
        List<ProductDTO> products = productService.getAllProduct();
        int statusCode = determineStatusCode(products); // Method to determine status code
        Map<String, Object> response = createResponse(statusCode, products);
        return ResponseEntity.status(statusCode).body(response);
    }


    //////////////// Non-Mapping methods ////////////////

    private void validateProductNode(ObjectNode productNode) {
        if (!productNode.has("name") || !productNode.has("description") || !productNode.has("price")) {
            throw new InvalidProductException("Product must have name, description, and price");
        }
        if (productNode.size() > 3) {
            throw new InvalidProductException("Product must not have additional attributes");
        }

        if (productNode.size() < 3) {
            throw new InvalidProductException("Request lacks the required parameters.");
        }
    }

    private int determineStatusCode(List<ProductDTO> products) {
        if (products.isEmpty()) {
            return 404; // Not Found
        }
        return 200; // OK
    }

    private Map<String, Object> createResponse(int statusCode, List<ProductDTO> products) {
        Map<String, Object> response = new HashMap<>();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        response.put("timestamp", timestamp);
        response.put("status", statusCode);
        response.put("error", getErrorMessage(statusCode));
        response.put("message", products);
        // response.put("path", "/api/resource");
        return response;
    }

    // Function for returning the appropriate Status Codes
    private String getErrorMessage(int statusCode) {
        switch (statusCode) {
            case 404:
                return "Not Found";
            case 200:
                return "OK";

            // TODO Add More

            default:
                return "Unknown Error";
        }
    }
}