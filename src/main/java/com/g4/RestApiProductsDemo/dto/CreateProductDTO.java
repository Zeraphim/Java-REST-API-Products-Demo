package com.g4.RestApiProductsDemo.dto;

public class CreateProductDTO {
    private final String name;
    private final String description;
    private final double price;
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



