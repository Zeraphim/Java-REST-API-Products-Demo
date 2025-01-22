package com.g4.RestApiScorecardsDemo.repository;

import com.g4.RestApiScorecardsDemo.entity.ScoreCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ScoreCardRepository extends JpaRepository<ScoreCard, Long> {

    // Preventing SQL Injection
    @Query("SELECT s FROM ScoreCard s WHERE s.name = :name")
    ScoreCard findByName(@Param("name") String name);
}