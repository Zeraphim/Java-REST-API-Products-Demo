package com.g4.RestApiProductsDemo.client;

import com.g4.RestApiProductsDemo.dto.ProductDTO;

import java.io.IOException;
import java.util.List;

public class MainHttpURLConnection {
    public static void main(String[] args) throws IOException {

        ProductHttpURLConnectionClient client = new ProductHttpURLConnectionClient();

        //Create new product
        System.out.println("\nCreating a new product:");
        ProductDTO createdProduct = client.createProduct("New Product", "A sample product description", 19.99, "Internal Code Sample HttpURLConnection");

        //create 2nd product
        ProductDTO createdProduct2 = client.createProduct("Product2", "2nd Product", 9999, "Internal Code Sample HttpURLConnection");
        System.out.println("Created Products: " + createdProduct.printProductDetails() +"\n\n" +createdProduct2.printProductDetails());

        //Fetch all products
        System.out.println("\nFetching all products...");
        List<ProductDTO> products = client.getAllProducts();
        products.forEach(product -> System.out.println(product.printProductDetails()));

        //Fetch product by ID
        System.out.println("\nFetching the created product by ID...");
        ProductDTO fetchedProduct = client.getProductById(1L);
        System.out.println("Fetched Product: " + fetchedProduct.printProductDetails());

        //Update product by ID
        System.out.println("\nUpdating a product by ID...");
        ProductDTO updatedProduct = client.updateProduct(1L, "Updated Product", "Updated description", 29.99);
        System.out.println("Updated Product: " + updatedProduct.printProductDetails());

        //Delete product by ID
        System.out.println("\nDeleting a product by ID...");
        String deleteResponse = client.deleteProduct(1L);
        System.out.println("Deleted Product Response: " + deleteResponse);
    }
}
