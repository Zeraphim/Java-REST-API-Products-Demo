package com.g4.RestApiProductsDemo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

// JsonIgnoreProperties - This annotation is used to either suppress serialization of properties (during serialization),
// or ignore processing of JSON properties read (during deserialization).
// @JsonIgnoreProperties(ignoreUnknown = false)
public class CreateProductDTO {

    // @NotBlank(message = "Name is mandatory")
    private final String name;
    // @NotBlank(message = "Description is mandatory")
    private final String description;
    // @NotNull(message = "Price is mandatory")
    private final double price;
    // @NotBlank(message = "Internal code is mandatory")
    private final String internalCode;

    // Constructors
    public CreateProductDTO(String name, String description, double price, String internalCode) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.internalCode = internalCode;
    }

    // Getters and Setters
    public String getName() {return name;}
    //public void setName(String name) {this.name = name;}
    public String getDescription() {return description;}
    //public void setDescription(String description) {this.description = description;}
    public double getPrice() {return price;}
    //public void setPrice(double price) {this.price = price;}
    public String getInternalCode() {return internalCode;}
    //public void setInternalCode(String internalCode) {this.internalCode = internalCode;}
}

/*
public void tryToModify() {
    // Uncommenting the following line will cause a compilation error because `id` is final
    //id = 100L; // Cannot assign a value to a final variable
}
*/



