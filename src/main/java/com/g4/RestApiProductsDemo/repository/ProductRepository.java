package com.g4.RestApiProductsDemo.repository;

import com.g4.RestApiProductsDemo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // Preventing SQL Injection
    @Query("SELECT s FROM Product s WHERE s.name = :name")
    Product findByName(@Param("name") String name);
}