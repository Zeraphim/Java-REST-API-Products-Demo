package com.g4.RestApiProductsDemo.exception;

import java.time.LocalDateTime;

public class CustomErrorResponse {
    private String status; // "error", "informational", "redirectional", "client error", "server error"
    private String message; // The message (if success) or blank (if failed)
    private LocalDateTime timestamp; // The current timestamp
    private int statusCode; // The HTTP status code

    // Constructor
    public CustomErrorResponse(String status, String message, int statusCode) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.statusCode = statusCode;
    }

    // Getters and Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}