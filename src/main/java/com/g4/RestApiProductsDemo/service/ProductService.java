package com.g4.RestApiProductsDemo.service;

import com.g4.RestApiProductsDemo.dto.CreateProductDTO;
import com.g4.RestApiProductsDemo.dto.ProductDTO;
import com.g4.RestApiProductsDemo.entity.Product;
import com.g4.RestApiProductsDemo.exception.ProductDeletionException;
import com.g4.RestApiProductsDemo.exception.ProductNotFoundException;
import com.g4.RestApiProductsDemo.exception.ProductUpdateException;
import com.g4.RestApiProductsDemo.exception.ResourceNotFoundException;
import com.g4.RestApiProductsDemo.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired // Field injection
    private ProductRepository productRepository;

/*
    private ProductRepository productRepository;
    @Autowired //Setter injection
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    private final ProductRepository productRepository;
    @Autowired // Constructor injection
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
*/

    /*@PostConstruct
    public void init() {
        if (productRepository == null) {
            throw new IllegalStateException("ProductRepository is not injected!");
        }
    }*/

    // Product Entity to ProductDTO (for client viewing/no internalCode)
    private ProductDTO mapToDTO(Product product) {
        return new ProductDTO(product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice());
    }
    // ProductDTO to Product Entity (for creating product/with internalCode)
    private Product mapToEntity(CreateProductDTO createProductDTO) {
        return new Product(createProductDTO.getName(),
                createProductDTO.getDescription(),
                createProductDTO.getPrice(),
                createProductDTO.getInternalCode());  // Store the internalCode in DB
    }

    // Get all products
    public List<ProductDTO> getAllProduct() {
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) {
            throw new ProductNotFoundException("No products found");
        }
        return products.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Get a single product by ID
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found"));
        return mapToDTO(product);
    }

    // Create a new product
    public ProductDTO createProduct(CreateProductDTO createProductDTO) {
        Product product = mapToEntity(createProductDTO);
        Product savedProduct = productRepository.save(product);
        return mapToDTO(savedProduct);
    }

    // Update a product
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        Product existingProduct = productRepository.findById(id).orElseThrow(() -> new ProductUpdateException("Product not found"));
        existingProduct.setName(productDTO.getName());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setPrice(productDTO.getPrice());
        Product updatedProduct = productRepository.save(existingProduct);
        return mapToDTO(updatedProduct);
    }

    // Delete a product
    public Map<String, Object> deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductDeletionException("Product not found"));
        productRepository.delete(product);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Product deleted successfully");
        Map<String, Object> deletedProduct = new HashMap<>();
        deletedProduct.put("id", product.getId());
        deletedProduct.put("name", product.getName());
        deletedProduct.put("price", product.getPrice());
        response.put("deletedProduct", deletedProduct);
        return response;
    }
}

/*
private Product mapToEntity(ProductDTO productDTO) {
    return new Product(productDTO.getName(),
            productDTO.getDescription(),
            productDTO.getPrice(),
            null);
}*/

/*@PostConstruct
public void init() {
    if (productRepository == null) {
        throw new IllegalStateException("ProductRepository is not injected!");
    }
}*/
