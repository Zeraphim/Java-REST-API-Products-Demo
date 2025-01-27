package com.g4.RestApiProductsDemo.service;

import com.g4.RestApiProductsDemo.dto.CreateProductDTO;
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
    private ProductRepository productRepository;

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
        return productRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    // Get a single product by ID
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
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
        Product existingProduct = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        existingProduct.setName(productDTO.getName());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setPrice(productDTO.getPrice());
        // existingProduct.setInternalCode(internalCode); // Update internalCode if necessary
        Product updatedProduct = productRepository.save(existingProduct);
        return mapToDTO(updatedProduct);
    }
    // Delete a product
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        productRepository.delete(product);
    }
}

/*
private Product mapToEntity(ProductDTO productDTO) {
    return new Product(productDTO.getName(),
            productDTO.getDescription(),
            productDTO.getPrice(),
            null);
}*/
