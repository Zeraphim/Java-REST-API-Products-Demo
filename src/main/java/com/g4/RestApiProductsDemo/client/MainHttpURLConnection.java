package com.g4.RestApiProductsDemo.client;

import com.g4.RestApiProductsDemo.dto.ProductDTO;

import java.io.IOException;
import java.util.List;

public class MainHttpURLConnection {
    public static void main(String[] args) throws IOException {

        ProductHttpURLConnectionClient client = new ProductHttpURLConnectionClient();

        System.out.println("\nCreating a new product:");
        ProductDTO createdProduct = client.createProduct("New Product", "A sample product description", 19.99, "Internal Code Sample HttpURLConnection");
        System.out.println("Created Product: " + createdProduct.printProductDetails());  // Print created product

        System.out.println("\nFetching all products...");
        // Fetch all products and print their details in Main
        List<ProductDTO> products = client.getAllProducts();
        products.forEach(product -> System.out.println(product.printProductDetails())); // Print details of each product


        System.out.println("\nFetching the created product by ID...");
        ProductDTO fetchedProduct = client.getProductById(1L);  // Print fetched product
        System.out.println("Fetched Product: " + fetchedProduct.printProductDetails());

        System.out.println("\nUpdating a product by ID...");
        ProductDTO updatedProduct = client.updateProduct(1L, "Updated Product", "Updated description", 29.99);
        System.out.println("Updated Product: " + updatedProduct.printProductDetails());

        System.out.println("\nDeleting a product by ID...");
        String deleteResponse = client.deleteProduct(1L);
        System.out.println("Deleted Product Response: " + deleteResponse);
    }
}
