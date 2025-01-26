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
            // 1. Get all products
            System.out.println("Fetching all products...");
            List<ProductDTO> products = productWebClient.getAllProducts();
            products.forEach(product ->System.out.println(product.printProductDetails()));

            // 2. Create a new product
            System.out.println("Creating a new product...");
            CreateProductDTO newProduct = new CreateProductDTO(null, "New Product", "This is a demo product created via RestTemplate.", 299.99, "Internal Code Sample RestTemplate");
            ProductDTO createdProduct = productWebClient.createProduct(newProduct);
            System.out.println("Created Product: " + createdProduct.printProductDetails());

            // 3. Fetch the product by ID
            System.out.println("Fetching the created product by ID...");
            ProductDTO fetchedProduct = productWebClient.getProductById(createdProduct.getId());
            System.out.println("Fetched Product: " + fetchedProduct.printProductDetails());

            // 4. Update the product
            System.out.println("Updating the product...");
            fetchedProduct.setPrice(399.99);
            ProductDTO updatedProduct = productWebClient.updateProduct(fetchedProduct.getId(), fetchedProduct);
            System.out.println("Updated Product: " + updatedProduct.printProductDetails());

            // 5. Delete the product
            System.out.println("Deleting the product...");
            productWebClient.deleteProduct(updatedProduct.getId());
            System.out.println("Deleted Product with ID: " + updatedProduct.getId());
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }
}
