package com.g4.RestApiProductsDemo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.g4.RestApiProductsDemo.exception.InvalidProductException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.g4.RestApiProductsDemo.dto.ProductDTO;
import com.g4.RestApiProductsDemo.exception.ResourceNotFoundException;
import com.g4.RestApiProductsDemo.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api/v2/product")
public class ProductControllerV2 {

    @Autowired
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    // Get all products
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProduct() {
        List<ProductDTO> products = productService.getAllProduct();
        return ResponseEntity.ok(products);
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
        ProductDTO productDTO = objectMapper.convertValue(productNode, ProductDTO.class);
        ProductDTO createdProduct = productService.createProduct(productDTO);
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
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

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
}