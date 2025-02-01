package com.g4.RestApiProductsDemo.controller;

import com.g4.RestApiProductsDemo.exception.*;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.io.IOException;
import java.nio.file.FileSystemException;
import java.rmi.RemoteException;


@RestControllerAdvice(assignableTypes = ProductControllerV3.class)
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(
                "client error", // status
                ex.getMessage(), // message
                HttpStatus.NOT_FOUND.value() // status code
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<CustomErrorResponse> handleIOException(IOException ex, WebRequest request) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(
                "server error", // status
                "An I/O error occurred: " + ex.getMessage(), // message
                HttpStatus.INTERNAL_SERVER_ERROR.value() // status code
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(FileSystemException.class)
    public ResponseEntity<CustomErrorResponse> handleFileSystemException(FileSystemException ex, WebRequest request) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(
                "server error", // status
                "A file system error occurred: " + ex.getMessage(), // message
                HttpStatus.INTERNAL_SERVER_ERROR.value() // status code
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RemoteException.class)
    public ResponseEntity<CustomErrorResponse> handleRemoteException(RemoteException ex, WebRequest request) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(
                "server error", // status
                "A remote error occurred: " + ex.getMessage(), // message
                HttpStatus.INTERNAL_SERVER_ERROR.value() // status code
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorResponse> handleGenericException(Exception ex, WebRequest request) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(
                "server error", // status
                "An unexpected error occurred: " + ex.getMessage(), // message
                HttpStatus.INTERNAL_SERVER_ERROR.value() // status code
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<CustomErrorResponse> handleBadRequestException(BadRequestException ex, WebRequest request) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(
                "client error", // status
                "Bad request: " + ex.getMessage(), // message
                HttpStatus.BAD_REQUEST.value() // status code
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // Custom Exceptions
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleProductNotFoundException(ProductNotFoundException ex, WebRequest request) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(
                "client error", // status
                ex.getMessage(), // message
                HttpStatus.NOT_FOUND.value() // status code
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProductUpdateException.class)
    public ResponseEntity<CustomErrorResponse> handleProductUpdateException(ProductUpdateException ex, WebRequest request) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(
                "client error", // status
                ex.getMessage(), // message
                HttpStatus.BAD_REQUEST.value() // status code
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProductDeletionException.class)
    public ResponseEntity<CustomErrorResponse> handleProductDeletionException(ProductDeletionException ex, WebRequest request) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(
                "client error", // status
                ex.getMessage(), // message
                HttpStatus.BAD_REQUEST.value() // status code
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}