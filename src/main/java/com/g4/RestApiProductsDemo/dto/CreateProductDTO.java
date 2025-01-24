package com.g4.RestApiProductsDemo.dto;

public class CreateProductDTO {

    private Long id;
    private String name;
    private String description;
    private double price;
    private String internalCode;

    // Constructors
    public CreateProductDTO(Long id, String name, String description, double price, String internalCode) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.internalCode = internalCode;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public String getInternalCode() {
        return internalCode;
    }
    public void setInternalCode(String internalCode) {
        this.internalCode = internalCode;
    }
}
