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

    // Product entity to ProductDTO
    // for client viewing (no internalCode)
    private ProductDTO mapToDTO(Product product) {
        return new ProductDTO(product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice());
    }

    // ProductDTO to Product entity
    // for creating product (with internalCode)
    private Product mapToEntity(CreateProductDTO createProductDTO) {
        return new Product(createProductDTO.getName(),
                createProductDTO.getDescription(),
                createProductDTO.getPrice(),
                //iha-hash si internal code dapat
                createProductDTO.getInternalCode());  // Store the internalCode
    }

    // Create a new product
    public ProductDTO createProductDTO(CreateProductDTO createProductDTO) {
        Product product = mapToEntity(createProductDTO);
        Product savedProduct = productRepository.save(product);
        return mapToDTO(savedProduct);
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

/*@Service
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
}*/

/*    // Convert ProductDTO to Product entity (include internalCode in entity)
    private Product mapToEntity(ProductDTO productDTO, String internalCode) {
        return new Product(productDTO.getName(),
                productDTO.getDescription(),
                productDTO.getPrice(),
                internalCode);  // Store the internalCode
    }*/