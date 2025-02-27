package com.g4.RestApiProductsDemo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.g4.RestApiProductsDemo.RabbitConfig.RabbitMQConfig;
import com.g4.RestApiProductsDemo.controller.Async.AsyncService;
import com.g4.RestApiProductsDemo.dto.CreateProductDTO;
import com.g4.RestApiProductsDemo.exception.InvalidProductException;
import com.g4.RestApiProductsDemo.repository.RabbitMQProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.g4.RestApiProductsDemo.dto.ProductDTO;
import com.g4.RestApiProductsDemo.exception.ResourceNotFoundException;
import com.g4.RestApiProductsDemo.service.ProductService;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api/v4/product")
public class ProductControllerV4 {

    @Autowired
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RabbitMQProducer rabbitMQProducer;

    @Autowired
    private AsyncService asyncService;

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    ///////////// Synchronous and Asynchronous Demo START /////////////

    // Beginner

    /*

    Server thread handling the request will sleep for 3 seconds (blocking the thread)
    and preventing it from handling other requests during this time.

    If multiple requests were made to this endpoint each request will block a server
    thread for 3 seconds leading to thread exhaustion under high load.

     */

    @GetMapping("/sync")
    public String syncDemo() throws InterruptedException {
        Thread.sleep(3000); // Wait for 3 seconds
        return "Process Completed";
    }

    // Novice

    /*

    Returns immediately and processes the request in a separate thread,
    allowing the server thread to handle other requests.

     */

    @GetMapping("/async-novice")
    public String asyncEndpoint() throws InterruptedException {
        asyncService.asyncMethod();
        return "Request is being processed asynchronously";
    }

    // Adept
    @GetMapping("/async-adept")
    public Mono<ResponseEntity<List<ProductDTO>>> getAllProducts() {
        Random random = new Random();
        int sleepTime = 1000 + random.nextInt(3000); // Generates a random number

        return Mono.delay(Duration.ofMillis(sleepTime))
                .flatMap(delay -> Mono.fromCallable(() -> productService.getAllProduct()))
                .map(products -> ResponseEntity.ok(products));
    }

    // Advanced
    @GetMapping("/async-advanced")
    public ResponseEntity<String> asyncAdvanced() {
        String message = "Process this message in the background";
        rabbitMQProducer.sendMessage(RabbitMQConfig.QUEUE_NAME, message);
        return ResponseEntity.ok("Request received. Processing in the background.");
    }

    // Async returning a single response
    // Best used for long-running tasks that require single result once the task is completed.
    @GetMapping("/asyncBasic")
    public CompletableFuture<String> asyncDemo() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(3000); // Wait for 3 seconds
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return "Process Completed";
        }, executorService);
    }

    // Async returning multiple event responses to client (Webflux)
    // Ideal for scenarios where you need to provide real-time updates to the client about the progress of a long-running task.
    @GetMapping(value = "/asyncEvent", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter asyncDemoTwo() {
        SseEmitter emitter = new SseEmitter();

        ProcessStatus initialStatus = new ProcessStatus(
                "Process started",
                Thread.currentThread().getName(),
                Thread.currentThread().getId()
        );

        try {
            emitter.send(initialStatus);
        } catch (IOException e) {
            emitter.completeWithError(e);
            return emitter;
        }

        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(3000); // Wait for 3 seconds
                ProcessStatus completedStatus = new ProcessStatus(
                        "Process completed",
                        Thread.currentThread().getName(),
                        Thread.currentThread().getId()
                );
                emitter.send(completedStatus);
            } catch (InterruptedException | IOException e) {
                Thread.currentThread().interrupt();
                emitter.completeWithError(e);
            }
            emitter.complete();
        }, executorService);

        return emitter;
    }

    ///////////// Synchronous and Asynchronous Demo END /////////////

    // Get all products
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProduct() {
        List<ProductDTO> products = productService.getAllProduct();
        return ResponseEntity.ok(products);
    }

    // Get a product by ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        ProductDTO product = productService.getProductById(id);
        if (product == null) {
            throw new ResourceNotFoundException("Product not found with id " + id);
        }
        return ResponseEntity.ok(product);
    }

    // Create a new product
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ObjectNode productNode) {
        validateProductNode(productNode);
        CreateProductDTO createProductDTO = objectMapper.convertValue(productNode, CreateProductDTO.class);
        ProductDTO createdProduct = productService.createProduct(createProductDTO);
        return ResponseEntity.ok(createdProduct);
    }

    // Update a product by ID
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ObjectNode productNode) {
        validateProductNode(productNode);
        ProductDTO productDTO = objectMapper.convertValue(productNode, ProductDTO.class);
        ProductDTO updatedProduct = productService.updateProduct(id, productDTO);
        return ResponseEntity.ok(updatedProduct);
    }

    // Delete a product by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    private void validateProductNode(ObjectNode productNode) {
        if (!productNode.has("name") || !productNode.has("description") || !productNode.has("price")) {
            throw new InvalidProductException("Product must have name, description, and price");
        }
        if (productNode.size() > 3) {
            throw new InvalidProductException("Product must not have additional attributes");
        }

        if (productNode.size() < 3) {
            throw new InvalidProductException("Request lacks the required parameters.");
        }
    }
}