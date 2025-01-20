package com.g4.RestApiScorecardsDemo.repository;

import com.g4.RestApiScorecardsDemo.entity.ScoreCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScoreCardRepository extends JpaRepository<ScoreCard, Long> {
}