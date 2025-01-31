package com.g4.RestApiProductsDemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@SpringBootConfiguration // Marks the class as a Spring Boot Configuration class.
//@EnableAutoConfiguration // Enables automatic configuration based on the dependencies in the classpath.
//@ComponentScan // Scans for components (service, controller, etc.) to register as beans.
public class RestApiProductsDemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(RestApiProductsDemoApplication.class, args);
	}
}


