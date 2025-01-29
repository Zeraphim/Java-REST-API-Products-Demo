package com.g4.RestApiProductsDemo.dto;

public class ProductDTO {
    private  Long id;
    private String name;
    private  String description;
    private double price;

    //Required for JSON de/serialization
    public ProductDTO(){}

    // Constructors
    public ProductDTO(Long id, String name, String description, double price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    // Getters and Setters
    public Long getId() {return id;}
    //public void setId(Long id) {this.id = id;}
    public String getName() {return name;}
    //public void setName(String name) {this.name = name;}
    public String getDescription() {return description;}
    //public void setDescription(String description) {this.description = description;}
    public double getPrice() {return price;}
    //public void setPrice(double price) {this.price = price;}

    public String printProductDetails() {
        return "Product Details:\n" +
                "----------------\n" +
                "ID: " + id + "\n" +
                "Name: " + name + "\n" +
                "Description: " + description + "\n" +
                "Price: $" + String.format("%.2f", price) + "\n" +
                "----------------";
    }
}

/*
public void tryToModify() {
    // Uncommenting the following line will cause a compilation error because `id` is final
    //id = 100L; // Cannot assign a value to a final variable
}
*/



