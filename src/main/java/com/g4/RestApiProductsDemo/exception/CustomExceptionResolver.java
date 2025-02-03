package com.g4.RestApiProductsDemo.exception;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Component
public class CustomExceptionResolver implements HandlerExceptionResolver {

    private static final Logger logger = LoggerFactory.getLogger(CustomExceptionResolver.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        String acceptHeader = request.getHeader("Accept");

        if (acceptHeader != null && acceptHeader.contains(MediaType.TEXT_HTML_VALUE)) {
            return handleHtmlResponse(ex, response);
        } else {
            return handleJsonResponse(ex, response);
        }
    }

    private ModelAndView handleHtmlResponse(Exception ex, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();
        if (ex instanceof ResourceNotFoundException) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            modelAndView.setViewName("error-404");
        } else if (ex instanceof BadRequestException) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            modelAndView.setViewName("error-400");
        } else {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            modelAndView.setViewName("error-500");
        }
        return modelAndView;
    }

    private ModelAndView handleJsonResponse(Exception ex, HttpServletResponse response) {
        try {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");

            CustomErrorResponse errorResponse;
            if (ex instanceof ResourceNotFoundException) {
                response.setStatus(HttpStatus.NOT_FOUND.value());
                errorResponse = new CustomErrorResponse("client error", ex.getMessage(), HttpStatus.NOT_FOUND.value());
            } else if (ex instanceof BadRequestException) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                errorResponse = new CustomErrorResponse("client error", "Bad request: " + ex.getMessage(), HttpStatus.BAD_REQUEST.value());
            } else {
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                errorResponse = new CustomErrorResponse("server error", "An unexpected error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
            }

            response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
        } catch (IOException e) {
            logger.error("Error writing JSON response", e);
        }
        return new ModelAndView();
    }
}