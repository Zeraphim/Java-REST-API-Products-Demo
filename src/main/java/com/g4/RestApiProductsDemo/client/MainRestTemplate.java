package com.g4.RestApiProductsDemo.client;

import com.g4.RestApiProductsDemo.dto.CreateProductDTO;
import com.g4.RestApiProductsDemo.dto.ProductDTO;

import java.util.List;

public class MainRestTemplate {

    public static void main(String[] args) {
        // Instantiate the API client
        ProductRestTemplateClient productWebClient = new ProductRestTemplateClient();

        // Example usage of the RestTemplate client
        try {

            // Create new product
            System.out.println("Creating 2 new products...");
            CreateProductDTO newProduct = new CreateProductDTO("New Product", "This is a demo product created via RestTemplate.", 299.99, "Internal Code Sample RestTemplate");
            ProductDTO createdProduct = productWebClient.createProduct(newProduct);
            System.out.println("Created Product: " + createdProduct.printProductDetails());

            // Get all products
            System.out.println("Fetching all products...");
            List<ProductDTO> products = productWebClient.getAllProducts();
            products.forEach(product ->System.out.println(product.printProductDetails()));

            // Fetch product by ID
            System.out.println("Fetching the created product by ID...");
            ProductDTO fetchedProduct = productWebClient.getProductById(createdProduct.getId());
            System.out.println("Fetched Product: " + fetchedProduct.printProductDetails());

            // Update product by ID
            System.out.println("Updating the product...");
            ProductDTO productDTO = new ProductDTO(fetchedProduct.getId(), fetchedProduct.getName(), fetchedProduct.getDescription(), 399.99);
            ProductDTO updatedProduct = productWebClient.updateProduct(productDTO.getId(), productDTO);
            System.out.println("Updated Product: " + updatedProduct.printProductDetails());

            // Delete product by ID
            System.out.println("Deleting the product...");
            productWebClient.deleteProduct(updatedProduct.getId());
            System.out.println("Deleted Product with ID: " + updatedProduct.getId());
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }
}
