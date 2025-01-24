// src/main/java/com/g4/RestApiProductsDemo/exception/ResourceNotFoundException.java
package com.g4.RestApiProductsDemo.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}