package com.g4.RestApiProductsDemo.service;

import com.g4.RestApiProductsDemo.dto.ProductDTO;
import com.g4.RestApiProductsDemo.entity.Product;
import com.g4.RestApiProductsDemo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository ProductRepository;

    // Convert Product entity to ProductDTO
    private ProductDTO mapToDTO(Product product) {
        return new ProductDTO(product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice());
    }

    // Convert ProductDTO to Product entity
    private Product mapToEntity(ProductDTO productDTO) {
        return new Product(productDTO.getName(),
                productDTO.getDescription(),
                productDTO.getPrice(),
        null);
    }

    // Get all product
    public List<ProductDTO> getAllProduct() {
        return ProductRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Get a single product by ID
    public ProductDTO getProductById(Long id) {
        Product product = ProductRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        return mapToDTO(product);
    }

    // Create a new product
    public ProductDTO createProduct(ProductDTO ProductDTO) {
        Product product = mapToEntity(ProductDTO);
        Product savedProduct = ProductRepository.save(product);
        return mapToDTO(savedProduct);
    }

    // Update an existing product
    public ProductDTO updateProduct(Long id, ProductDTO ProductDTO) {
        Product existingProduct = ProductRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        existingProduct.setName(ProductDTO.getName());
        existingProduct.setDescription(ProductDTO.getDescription());
        existingProduct.setPrice(ProductDTO.getPrice());
        Product updatedProduct = ProductRepository.save(existingProduct);
        return mapToDTO(updatedProduct);
    }

    // Delete a product
    public void deleteProduct(Long id) {
        Product product = ProductRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        ProductRepository.delete(product);
    }
}
