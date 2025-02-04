package com.g4.RestApiProductsDemo.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.g4.RestApiProductsDemo.entity.Product;
import com.g4.RestApiProductsDemo.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Product testProduct;

    @BeforeEach
    void setup() {
        //Deletes all items in repo so each test has a fresh database
        productRepository.deleteAll();
        //Instantiate an entity in the database to test GET requests
        testProduct = new Product("Test Product", "A test description", 99.99, "TEST123");
        productRepository.save(testProduct);
    }

    @Test
    void testGetAllProducts() throws Exception {
        mockMvc.perform(get("/api/v2/product"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Test Product"));
    }

    @Test
    void testGetProductById() throws Exception {
        mockMvc.perform(get("/api/v2/product/" + testProduct.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Product"));
    }

    @Test
    void testGetProductById_NotFound() throws Exception {
        mockMvc.perform(get("/api/v2/product/99999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Resource not found: Product not found with id 99999"));
    }

    @Test
    void testCreateProduct() throws Exception {
        ObjectNode newProduct = objectMapper.createObjectNode();
        newProduct.put("name", "New Product");
        newProduct.put("description", "New Description");
        newProduct.put("price", 49.99);

        mockMvc.perform(post("/api/v2/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Product"));
    }

    @Test
    void testCreateProduct_ExtraFields() throws Exception {
        ObjectNode newProduct = objectMapper.createObjectNode();
        newProduct.put("name", "New Product");
        newProduct.put("description", "New Description");
        newProduct.put("price", 49.99);
        newProduct.put("extraField", "Not Allowed");

        mockMvc.perform(post("/api/v2/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newProduct)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateProduct_MissingFields() throws Exception {
        ObjectNode newProduct = objectMapper.createObjectNode();
        newProduct.put("name", "New Product");
        newProduct.put("price", 49.99);

        mockMvc.perform(post("/api/v2/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newProduct)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateProduct() throws Exception {
        ObjectNode updateProduct = objectMapper.createObjectNode();
        updateProduct.put("name", "Updated Name");
        updateProduct.put("description", "Updated Description");
        updateProduct.put("price", 79.99);

        mockMvc.perform(put("/api/v2/product/" + testProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateProduct)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Updated Name"));
    }

    @Test
    void testUpdateProduct_NoChanges() throws Exception {
        ObjectNode updateProduct = objectMapper.createObjectNode();
        updateProduct.put("name", "Test Product");
        updateProduct.put("description", "A test description");
        updateProduct.put("price", 99.99);

        mockMvc.perform(put("/api/v2/product/" + testProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateProduct)))
                .andExpect(status().isCreated());
    }

    @Test
    void testDeleteProduct() throws Exception {
        mockMvc.perform(delete("/api/v2/product/" + testProduct.getId()))
                .andExpect(status().isNoContent());

        Optional<Product> deletedProduct = productRepository.findById(testProduct.getId());
        assertThat(deletedProduct).isEmpty();
    }

    @Test
    void testDeleteProduct_AlreadyDeleted() throws Exception {
        mockMvc.perform(delete("/api/v2/product/" + testProduct.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(delete("/api/v2/product/" + testProduct.getId()))
                .andExpect(status().isNotFound());
    }

}
