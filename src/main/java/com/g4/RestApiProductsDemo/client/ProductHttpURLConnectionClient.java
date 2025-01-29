package com.g4.RestApiProductsDemo.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.g4.RestApiProductsDemo.dto.CreateProductDTO;
import com.g4.RestApiProductsDemo.dto.ProductDTO;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.g4.RestApiProductsDemo.client.UrlConstants.BASE_URL;

public class ProductHttpURLConnectionClient {

    // Jackson ObjectMapper for parsing JSON
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Method to create HttpURLConnection with request configuration
    private HttpURLConnection getHttpURLConnection(String method, String jsonBody, URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        connection.setRequestProperty("Content-Type", "application/json");

        connection.setDoOutput(true);

        if (jsonBody != null && !jsonBody.isEmpty()) {
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
        }
        return connection;
    }

    // Helper method to send HTTP requests and return JSON response as a string
    private String sendHttpRequest(String urlString, String method, String jsonBody) throws IOException {
        URL url = URI.create(urlString).toURL();
        HttpURLConnection connection = getHttpURLConnection(method, jsonBody, url);

        int responseCode = connection.getResponseCode();
        InputStream responseStream = (responseCode >= 200 && responseCode < 300) ? connection.getInputStream() : connection.getErrorStream();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(responseStream, StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();
        }
    }

    // Helper method to convert JSON response to ProductDTO
    private ProductDTO convertToProductDTO(String jsonResponse) throws IOException {
        return objectMapper.readValue(jsonResponse, ProductDTO.class);
    }

    // Get all products
    public List<ProductDTO> getAllProducts() throws IOException {
        String response = sendHttpRequest(BASE_URL, "GET", null);
        // Parse the JSON response to a list of ProductDTOs
        return objectMapper.readValue(response, objectMapper.getTypeFactory().constructCollectionType(List.class, ProductDTO.class));
    }

    // Get a product by ID
    public ProductDTO getProductById(Long id) throws IOException {
        String response = sendHttpRequest(BASE_URL + "/" + id, "GET", null);
        System.out.println(response);
        return convertToProductDTO(response);
    }

    // Create a new product
    public ProductDTO createProduct(String name, String description, double price, String internalCode) throws IOException {
        CreateProductDTO productDTO = new CreateProductDTO(name, description, price, internalCode);
        String jsonBody = objectMapper.writeValueAsString(productDTO); // Convert DTO to JSON
        String response = sendHttpRequest(BASE_URL, "POST", jsonBody);
        return convertToProductDTO(response);
    }

    // Update an existing product
    public ProductDTO updateProduct(Long id, String name, String description, double price) throws IOException {
        ProductDTO productDTO = new ProductDTO(id, name, description, price);
        String jsonBody = objectMapper.writeValueAsString(productDTO); // Convert DTO to JSON
        String response = sendHttpRequest(BASE_URL + "/" + id, "PUT", jsonBody);
        return convertToProductDTO(response);
    }


    // Create a new product
//    public ProductDTO createProduct(String name, String description, double price, String internalCode) throws IOException {
//        String jsonBody = String.format(
//                "{\"name\":\"%s\", \"description\":\"%s\", \"price\":%.2f, \"internalCode\":\"%s\"}",
//                name, description, price, internalCode
//        );
//        String response = sendHttpRequest(BASE_URL, "POST", jsonBody);
//        return convertToProductDTO(response);
//    }

    // Update an existing product
//    public ProductDTO updateProduct(Long id, String name, String description, double price) throws IOException {
//        String jsonBody = String.format(
//                "{\"name\":\"%s\", \"description\":\"%s\", \"price\":%.2f}",
//                name, description, price
//        );
//        String response = sendHttpRequest(BASE_URL + "/" + id, "PUT", jsonBody);
//        return convertToProductDTO(response);
//    }

    // Delete a product
    public String deleteProduct(Long id) throws IOException {
        return sendHttpRequest(BASE_URL + "/" + id, "DELETE", null);
    }
}
