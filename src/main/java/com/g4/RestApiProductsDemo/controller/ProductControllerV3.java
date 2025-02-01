package com.g4.RestApiProductsDemo.controller;

import com.g4.RestApiProductsDemo.dto.CreateProductDTO;
import com.g4.RestApiProductsDemo.exception.CustomErrorResponse;
import com.g4.RestApiProductsDemo.exception.ResourceNotFoundException;
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
import java.util.List;

@RestController
@RequestMapping("/api/v3/product")
public class ProductControllerV3 {

    @Autowired
    private ProductService productService;

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
    public ResponseEntity<ProductDTO> createProduct(@RequestBody CreateProductDTO createProductDTO) {
        ProductDTO createdProduct = productService.createProduct(createProductDTO);
        return ResponseEntity.ok(createdProduct);
    }

    // Update a product
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        ProductDTO updatedProduct = productService.updateProduct(id, productDTO);
        return ResponseEntity.ok(updatedProduct);
    }

    // Delete a product
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    //////////////////// EXCEPTION HANDLING ////////////////////

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(
                "client error", // status
                ex.getMessage(), // message
                HttpStatus.NOT_FOUND.value() // status code
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<CustomErrorResponse> handleIOException(IOException ex, WebRequest request) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(
                "server error", // status
                "An I/O error occurred: " + ex.getMessage(), // message
                HttpStatus.INTERNAL_SERVER_ERROR.value() // status code
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(FileSystemException.class)
    public ResponseEntity<CustomErrorResponse> handleFileSystemException(FileSystemException ex, WebRequest request) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(
                "server error", // status
                "A file system error occurred: " + ex.getMessage(), // message
                HttpStatus.INTERNAL_SERVER_ERROR.value() // status code
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RemoteException.class)
    public ResponseEntity<CustomErrorResponse> handleRemoteException(RemoteException ex, WebRequest request) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(
                "server error", // status
                "A remote error occurred: " + ex.getMessage(), // message
                HttpStatus.INTERNAL_SERVER_ERROR.value() // status code
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorResponse> handleGenericException(Exception ex, WebRequest request) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(
                "server error", // status
                "An unexpected error occurred: " + ex.getMessage(), // message
                HttpStatus.INTERNAL_SERVER_ERROR.value() // status code
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}