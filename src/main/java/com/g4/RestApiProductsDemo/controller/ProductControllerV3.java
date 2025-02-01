package com.g4.RestApiProductsDemo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.g4.RestApiProductsDemo.dto.CreateProductDTO;
import com.g4.RestApiProductsDemo.exception.*;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.g4.RestApiProductsDemo.dto.ProductDTO;
import com.g4.RestApiProductsDemo.service.ProductService;
import org.springframework.web.context.request.WebRequest;

import java.io.IOException;
import java.nio.file.FileSystemException;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v3/product")
public class ProductControllerV3 {

    @Autowired
    private ProductService productService;

    // For the createProduct
    @Autowired
    private ObjectMapper objectMapper;

    // Get all products
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProduct() {
        try {
            List<ProductDTO> products = productService.getAllProduct();
            return ResponseEntity.ok(products);
        } catch (ProductNotFoundException ex) {
            throw new ResourceNotFoundException("Empty Product List");
        }
    }

    // Get a product by ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        try {
            ProductDTO product = productService.getProductById(id);
            return ResponseEntity.ok(product);
        } catch (ProductNotFoundException ex) {
            throw new ResourceNotFoundException("Product not found with id " + id);
        }
    }

    // Create a new product
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ObjectNode productNode) throws BadRequestException {
        validateProductNode(productNode);
        CreateProductDTO createProductDTO = objectMapper.convertValue(productNode, CreateProductDTO.class);
        ProductDTO createdProduct = productService.createProduct(createProductDTO);
        return ResponseEntity.ok(createdProduct);
    }

    // Update a product
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ObjectNode productNode) throws BadRequestException {
        try {
            validateProductNode(productNode);
            ProductDTO productDTO = objectMapper.convertValue(productNode, ProductDTO.class);
            ProductDTO updatedProduct = productService.updateProduct(id, productDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(updatedProduct);
        } catch (ProductUpdateException ex) {
            throw new ProductUpdateException("Product not found");
        }
    }

    // Delete a product
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        } catch (ProductDeletionException ex) {
            throw new ProductDeletionException("Product not found");
        }
    }

    // Authenticated Endpoint
    @GetMapping("/secured")
    public ResponseEntity<List<ProductDTO>> getAllProductSecured() {
        try {
            List<ProductDTO> products = productService.getAllProduct();
            return ResponseEntity.ok(products);
        } catch (ProductNotFoundException ex) {
            throw new ResourceNotFoundException("Empty Product List");
        }
    }

    ////////// OTHER EXCEPTIONS //////////

    // Trigger IOException
    @GetMapping("/trigger-io-exception")
    public void triggerIOException() throws IOException {
        throw new IOException("Simulated I/O exception");
    }

    // Trigger FileSystemException
    @GetMapping("/trigger-fs-exception")
    public void triggerFileSystemException() throws FileSystemException {
        throw new FileSystemException("Simulated file system exception");
    }

    // Trigger RemoteException
    @GetMapping("/trigger-remote-exception")
    public void triggerRemoteException() throws RemoteException {
        throw new RemoteException("Simulated remote exception");
    }

    // Trigger Generic Exception
    @GetMapping("/trigger-generic-exception")
    public void triggerGenericException() throws Exception {
        throw new Exception("Simulated generic exception");
    }

    //////////////////// VALIDATE Request Body ////////////////////

    private void validateProductNode(ObjectNode productNode) throws BadRequestException {
        if (!productNode.has("name") || !productNode.has("description") || !productNode.has("price")) {
            throw new BadRequestException("Product must have name, description, and price");
        }
        if (productNode.size() > 3) {
            throw new BadRequestException("Product must not have additional attributes");
        }
        if (productNode.size() < 3) {
            throw new BadRequestException("Request lacks the required parameters.");
        }
        if (!productNode.get("name").isTextual()) {
            throw new BadRequestException("Name must be a string");
        }
        if (!productNode.get("description").isTextual()) {
            throw new BadRequestException("Description must be a string");
        }
        if (!productNode.get("price").isDouble() && !productNode.get("price").isFloat() && !productNode.get("price").isInt()) {
            throw new BadRequestException("Price must be a number");
        }
        if (productNode.get("price").asDouble() < 0) {
            throw new BadRequestException("Price must be a positive number");
        }
        if (productNode.get("price").asDouble() == 0) {
            throw new BadRequestException("Price cannot be zero");
        }
    }
}