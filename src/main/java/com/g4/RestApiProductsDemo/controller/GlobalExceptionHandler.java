package com.g4.RestApiProductsDemo.controller;

import com.g4.RestApiProductsDemo.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.IOException;
import java.nio.file.FileSystemException;
import java.rmi.RemoteException;


@RestControllerAdvice(assignableTypes = ProductControllerV3.class)
public class GlobalExceptionHandler {

    // Logger
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Custom error webpage
    @Autowired
    private CustomExceptionResolver customExceptionResolver;

    // Returning HTML custom error pages
    private boolean isBrowserRequest(HttpServletRequest request) {
        String acceptHeader = request.getHeader("Accept");
        return acceptHeader != null && acceptHeader.contains(MediaType.TEXT_HTML_VALUE);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public Object handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request, HttpServletResponse response) {
        if (isBrowserRequest(request)) {
            return customExceptionResolver.resolveException(request, response, null, ex);
        } else {
            logger.error("Resource Not Found Exception: {}", ex.getMessage());
            CustomErrorResponse errorResponse = new CustomErrorResponse("client error", ex.getMessage(), HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }

    @ExceptionHandler(IOException.class)
    public Object handleIOException(IOException ex, HttpServletRequest request, HttpServletResponse response) {
        if (isBrowserRequest(request)) {
            return customExceptionResolver.resolveException(request, response, null, ex);
        } else {
            logger.error("I/O Exception: {}", ex.getMessage());
            CustomErrorResponse errorResponse = new CustomErrorResponse("server error", "An I/O error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ExceptionHandler(FileSystemException.class)
    public Object handleFileSystemException(FileSystemException ex, HttpServletRequest request, HttpServletResponse response) {
        if (isBrowserRequest(request)) {
            return customExceptionResolver.resolveException(request, response, null, ex);
        } else {
            logger.error("File System Exception: {}", ex.getMessage());
            CustomErrorResponse errorResponse = new CustomErrorResponse("server error", "A file system error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ExceptionHandler(RemoteException.class)
    public Object handleRemoteException(RemoteException ex, HttpServletRequest request, HttpServletResponse response) {
        if (isBrowserRequest(request)) {
            return customExceptionResolver.resolveException(request, response, null, ex);
        } else {
            logger.error("Remote Exception: {}", ex.getMessage());
            CustomErrorResponse errorResponse = new CustomErrorResponse("server error", "A remote error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ExceptionHandler(BadRequestException.class)
    public Object handleBadRequestException(BadRequestException ex, HttpServletRequest request, HttpServletResponse response) {
        if (isBrowserRequest(request)) {
            return customExceptionResolver.resolveException(request, response, null, ex);
        } else {
            logger.error("Bad Request Exception: {}", ex.getMessage());
            CustomErrorResponse errorResponse = new CustomErrorResponse("client error", "Bad request: " + ex.getMessage(), HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public Object handleProductNotFoundException(ProductNotFoundException ex, HttpServletRequest request, HttpServletResponse response) {
        if (isBrowserRequest(request)) {
            return customExceptionResolver.resolveException(request, response, null, ex);
        } else {
            logger.error("Product Not Found Exception: {}", ex.getMessage());
            CustomErrorResponse errorResponse = new CustomErrorResponse("client error", ex.getMessage(), HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }

    @ExceptionHandler(ProductUpdateException.class)
    public Object handleProductUpdateException(ProductUpdateException ex, HttpServletRequest request, HttpServletResponse response) {
        if (isBrowserRequest(request)) {
            return customExceptionResolver.resolveException(request, response, null, ex);
        } else {
            logger.error("Product Update Exception: {}", ex.getMessage());
            CustomErrorResponse errorResponse = new CustomErrorResponse("client error", ex.getMessage(), HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @ExceptionHandler(ProductDeletionException.class)
    public Object handleProductDeletionException(ProductDeletionException ex, HttpServletRequest request, HttpServletResponse response) {
        if (isBrowserRequest(request)) {
            return customExceptionResolver.resolveException(request, response, null, ex);
        } else {
            logger.error("Product Deletion Exception: {}", ex.getMessage());
            CustomErrorResponse errorResponse = new CustomErrorResponse("client error", ex.getMessage(), HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public Object handleNoHandlerFoundException(NoHandlerFoundException ex, HttpServletRequest request, HttpServletResponse response) throws BadRequestException {
        if (isBrowserRequest(request)) {
            return customExceptionResolver.resolveException(request, response, null, ex);
        } else {
            logger.error("No Handler Found Exception: {}", ex.getMessage());
            throw new BadRequestException("Invalid endpoint");
        }
    }


    // Problem Detail Demo
    @ExceptionHandler(ProblemDetailExceptionDemo.class)
    public ProblemDetail handleIllegalArgument(ProblemDetailExceptionDemo ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Invalid Request");
        problem.setDetail(ex.getMessage());
        problem.setProperty("timestamp", java.time.LocalDateTime.now().toString());
        return problem;
    }
}