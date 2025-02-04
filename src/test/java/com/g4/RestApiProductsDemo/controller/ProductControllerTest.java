package com.g4.RestApiProductsDemo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.g4.RestApiProductsDemo.dto.CreateProductDTO;
import com.g4.RestApiProductsDemo.dto.ProductDTO;
import com.g4.RestApiProductsDemo.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class) // Automatically initializes mocks
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductControllerV1 productController;

    private ProductDTO productDTO;
    private CreateProductDTO createProductDTO;

    @BeforeEach
    void setUp() {
        // Prepare test data
        productDTO = new ProductDTO(1L, "Product 1", "Description for product 1", 100.0);
        createProductDTO = new CreateProductDTO("Product 1", "Description for product 1", 100.0, "ABC123");
    }

    @Test
    void getAllProduct() {
        // Mock the service to return a list of products
        when(productService.getAllProduct()).thenReturn(List.of(productDTO));

        // Simulate the controller's method and check the result
        ResponseEntity<List<ProductDTO>> responseEntity = productController.getAllProduct();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(1, responseEntity.getBody().size());
        assertEquals("Product 1", responseEntity.getBody().get(0).getName());
        assertEquals("Description for product 1", responseEntity.getBody().get(0).getDescription());
        assertEquals(100.0, responseEntity.getBody().get(0).getPrice());
    }

    @Test
    void getProductById() {
        // Mock the service to return a product by id
        when(productService.getProductById(1L)).thenReturn(productDTO);

        // Simulate the controller's method and check the result
        ResponseEntity<ProductDTO> responseEntity = productController.getProductById(1L);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(1L, responseEntity.getBody().getId());
        assertEquals("Product 1", responseEntity.getBody().getName());
        assertEquals("Description for product 1", responseEntity.getBody().getDescription());
        assertEquals(100.0, responseEntity.getBody().getPrice());
    }

    @Test
    void createProduct() throws Exception {
        // Mock the service to return the created product
        when(productService.createProduct(createProductDTO)).thenReturn(productDTO);

        // Simulate the controller's method and check the result
        ResponseEntity<ProductDTO> responseEntity = productController.createProduct(createProductDTO);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("Product 1", responseEntity.getBody().getName());
        assertEquals("Description for product 1", responseEntity.getBody().getDescription());
        assertEquals(100.0, responseEntity.getBody().getPrice());
    }

    @Test
    void updateProduct() throws Exception {
        // Mock the service to return the updated product
        ProductDTO updatedProductDTO = new ProductDTO(1L, "Updated Product", "Updated Description", 120.0);
        when(productService.updateProduct(1L, productDTO)).thenReturn(updatedProductDTO);

        // Simulate the controller's method and check the result
        ResponseEntity<ProductDTO> responseEntity = productController.updateProduct(1L, productDTO);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Updated Product", responseEntity.getBody().getName());
        assertEquals("Updated Description", responseEntity.getBody().getDescription());
        assertEquals(120.0, responseEntity.getBody().getPrice());
    }

    @Test
    void deleteProduct() {
        // Simulate the controller's method
        ResponseEntity<Void> responseEntity = productController.deleteProduct(1L);

        // Verify that the service's delete method was called
        verify(productService).deleteProduct(1L);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }
}
