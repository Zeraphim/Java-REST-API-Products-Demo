package com.g4.RestApiProductsDemo.service;

import com.g4.RestApiProductsDemo.dto.CreateProductDTO;
import com.g4.RestApiProductsDemo.dto.ProductDTO;
import com.g4.RestApiProductsDemo.entity.Product;
import com.g4.RestApiProductsDemo.exception.ProductDeletionException;
import com.g4.RestApiProductsDemo.exception.ProductNotFoundException;
import com.g4.RestApiProductsDemo.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;
    private Product product;
    private CreateProductDTO createProductDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Setup for Product
        product = new Product("Test Product", "Description of Test Product", 20.0, "INTERNAL123");
        createProductDTO = new CreateProductDTO("Test Product", "Description of Test Product", 20.0, "INTERNAL123");
    }

    @Test
    void testGetProductById() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        ProductDTO productDTO = productService.getProductById(1L);
        assertNotNull(productDTO);
        assertEquals("Test Product", productDTO.getName());
    }

    @Test
    void testGetProductById_NotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(1L));
    }

    @Test
    void testCreateProduct() {
        when(productRepository.save(any(Product.class))).thenReturn(product);
        ProductDTO productDTO = productService.createProduct(createProductDTO);
        assertNotNull(productDTO);
        assertEquals("Test Product", productDTO.getName());
    }

    @Test
    void testUpdateProduct() {
        Product updatedProduct = new Product("Updated Product", "Updated Description", 25.0, "INTERNAL456");
        ProductDTO updatedProductDTO = new ProductDTO(1L, "Updated Product", "Updated Description", 25.0);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        ProductDTO result = productService.updateProduct(1L, updatedProductDTO);

        assertNotNull(result);
        assertEquals("Updated Product", result.getName());
    }

    @Test
    void testDeleteProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        doNothing().when(productRepository).delete(product);

        Map<String, Object> response = productService.deleteProduct(1L);

        assertTrue(response.containsKey("message"));
        assertEquals("Product deleted successfully", response.get("message"));
    }

    @Test
    void testDeleteProduct_NotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ProductDeletionException.class, () -> productService.deleteProduct(1L));
    }

}
