package com.g4.RestApiProductsDemo.repository;

import com.g4.RestApiProductsDemo.entity.Product;
import com.g4.RestApiProductsDemo.service.ProductService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class) // This tells JUnit to use Mockito's extensions
public class ProductRepositoryTest {

    @Mock
    private ProductRepository productRepository;  // Mock the repository

    @InjectMocks
    private Product product;

    // Best practice
    @BeforeEach
    void setUp() { // Initialization
        product = new Product("Laptop", "High-end laptop", 1500.00, "ABC123");
        productRepository.save(product);
    }

    @AfterEach
    void tearDown() { // Cleaning
        productRepository.deleteAll();
    }

    // given (provided) - when (there is some execution) - then (there will be response)

    // Test case: Find product by name when found
    @Test
    void testFindByName_Found() {
        // Given
        when(productRepository.findByName("Laptop")).thenReturn(product);

        // When
        Product foundProduct = productRepository.findByName("Laptop");

        // Then
        assertThat(foundProduct).isNotNull();
        assertThat(foundProduct.getName()).isEqualTo(product.getName());
        assertThat(foundProduct.getPrice()).isEqualTo(product.getPrice());

        verify(productRepository, times(1)).findByName("Laptop");  // Verify interaction
    }

    // Test case: Find product by name when not found
    @Test
    void testFindByName_NotFound() {
        // Given
        when(productRepository.findByName("NonExistentProduct")).thenReturn(null);

        // When
        Product foundProduct = productRepository.findByName("NonExistentProduct");

        // Then
        assertThat(foundProduct).isNull();

        verify(productRepository, times(1)).findByName("NonExistentProduct");  // Verify interaction
    }

    // Test case: Saving a product with an empty name
    @Test
    void testFindByName_EmptyName() {
        // Given
        Product emptyNameProduct = new Product("", "Empty name product", 100.00, "XYZ123");
        when(productRepository.findByName("")).thenReturn(emptyNameProduct);

        // When
        Product foundProduct = productRepository.findByName("");

        // Then
        assertThat(foundProduct).isNotNull();
        assertThat(foundProduct.getName()).isEqualTo(emptyNameProduct.getName());

        verify(productRepository, times(1)).findByName("");  // Verify interaction
    }
}

/*// Test case: Saving a product with an invalid (negative) price
@Test
void testSaveProduct_WithNegativePrice() {
    // Given
    Product invalidProduct = new Product("Smartphone", "Flagship smartphone", -500.00, "DEF456");
    when(productRepository.save(invalidProduct)).thenReturn(invalidProduct);  // Mock saving

    // When
    Product savedProduct = productRepository.save(invalidProduct);

    // Then
    assertThat(savedProduct).isNotNull();
    assertThat(savedProduct.getPrice()).isEqualTo(-500.00);

    verify(productRepository, times(1)).save(invalidProduct);  // Verify interaction
}*/
