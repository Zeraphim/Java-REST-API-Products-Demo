package com.g4.RestApiProductsDemo.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.g4.RestApiProductsDemo.dto.CreateProductDTO;
import com.g4.RestApiProductsDemo.exception.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import com.g4.RestApiProductsDemo.dto.ProductDTO;
import com.g4.RestApiProductsDemo.service.ProductService;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v2/product")
public class ProductControllerV2 {

    @Autowired
    private ProductService productService;

    // For the createProduct
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
            throw new ProductUpdateException("Product update failed");
        }
    }

    // Delete a product
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        } catch (ProductDeletionException ex) {
            throw new ProductDeletionException("Product deletion failed");
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

    //////////////////// EXCEPTION HANDLING ////////////////////

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> handleBadRequestException(BadRequestException ex, WebRequest request) {
        return new ResponseEntity<>("Bad request: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>("Resource not found: " + ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<String> handleIOException(IOException ex, WebRequest request) {
        return new ResponseEntity<>("An I/O error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(FileSystemException.class)
    public ResponseEntity<String> handleFileSystemException(FileSystemException ex, WebRequest request) {
        return new ResponseEntity<>("A file system error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RemoteException.class)
    public ResponseEntity<String> handleRemoteException(RemoteException ex, WebRequest request) {
        return new ResponseEntity<>("A remote error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex, WebRequest request) {
        return new ResponseEntity<>("An unexpected error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Custom Exceptions
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<String> handleProductNotFoundException(ProductNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProductUpdateException.class)
    public ResponseEntity<String> handleProductUpdateException(ProductUpdateException ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProductDeletionException.class)
    public ResponseEntity<String> handleProductDeletionException(ProductDeletionException ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}