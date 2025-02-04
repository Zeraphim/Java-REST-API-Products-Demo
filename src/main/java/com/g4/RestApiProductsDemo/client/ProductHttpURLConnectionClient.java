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
        //HttpURLConnection -> subclass of URLConnection
        //Cannot instantiate an HttpURLConnection object directly, no public constructor
        HttpURLConnection connection = (HttpURLConnection) url.openConnection(); //Returns a URLConnection Object

        connection.setRequestMethod(method); // Set the request method type

        if (jsonBody != null && !jsonBody.isEmpty()) {
            connection.setRequestProperty("Content-Type", "application/json"); //Set the format for the request
            connection.setDoOutput(true); //Tell the HttpURLConnection object that the request has a body

            //try-with-resources to automatically close OutputStream
            //Closing OutputStream automatically sends request with the provided body
            try (OutputStream os = connection.getOutputStream()) {

                //Write the JSON request into the OutputStream of the connection object
                byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
        }
        return connection;
    }

    // Helper method to send HTTP requests and return JSON response as a string
    private String sendHttpRequest(String urlString, String method, String jsonBody) throws IOException {
        //Creates a URL object
        URL url = URI.create(urlString).toURL();
        HttpURLConnection connection = getHttpURLConnection(method, jsonBody, url);

        //getResponseCode() sends the request of the connection object, if not already by getOutputStream()
        int responseCode = connection.getResponseCode();
        System.out.println("Response Code: " + responseCode); // print out response code

        //Get response from the InputStream of the connection object
        //getInputStream() can also send the request
        //If there is an error, getInputStream() throws an IOException
        InputStream responseStream = (responseCode >= 200 && responseCode < 300) ? connection.getInputStream() : connection.getErrorStream();

        //Convert the responseStream into String
        //InputStreamReader converts bytes to characters for BufferedReader
        try (BufferedReader br = new BufferedReader(new InputStreamReader(responseStream, StandardCharsets.UTF_8))) {

            //Represents JSON response in StringBuilder format
            //StringBuilder is a mutable set of characters,
            //as opposed to the immutable String type
            StringBuilder response = new StringBuilder();
            String responseLine; //String placeholder

            //Append all lines to StringBuilder
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            if (responseCode >300 ){
                System.err.println("Error Response: " + response);
            }
            //Return StringBuilder as String
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
        // Parse the JSON string response to a list of ProductDTOs
        return objectMapper.readValue(response, objectMapper.getTypeFactory().constructCollectionType(List.class, ProductDTO.class));
    }

    // Get a product by ID
    public ProductDTO getProductById(Long id) throws IOException {
        String response = sendHttpRequest(BASE_URL + "/" + id, "GET", null);
        return convertToProductDTO(response);
    }

    // Create a new product
    public ProductDTO createProduct(String name, String description, double price, String internalCode) throws IOException {
        CreateProductDTO productDTO = new CreateProductDTO(name, description, price, internalCode);
        String jsonBody = objectMapper.writeValueAsString(productDTO);

//        String jsonBody = String.format(
//                "{\"name\":\"%s\", \"description\":\"%s\", \"price\":%.2f, \"internalCode\":\"%s\"}",
//                name, description, price, internalCode
//        );

        String response = sendHttpRequest(BASE_URL, "POST", jsonBody);
        return convertToProductDTO(response);
    }

    // Update an existing product
    public ProductDTO updateProduct(Long id, String name, String description, double price) throws IOException {
        ProductDTO productDTO = new ProductDTO(id, name, description, price);
        String jsonBody = objectMapper.writeValueAsString(productDTO);

//        String jsonBody = String.format(
//                "{\"name\":\"%s\", \"description\":\"%s\", \"price\":%.2f}",
//                name, description, price
//        );

        String response = sendHttpRequest(BASE_URL + "/" + id, "PUT", jsonBody);
        return convertToProductDTO(response);
    }

    // Delete a product
    public String deleteProduct(Long id) throws IOException {
        return sendHttpRequest(BASE_URL + "/" + id, "DELETE", null);
    }

}
