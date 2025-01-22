package com.g4.AsyncRestApiScorecards.repository;

import com.g4.AsyncRestApiScorecards.entity.ScoreCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScoreCardRepository extends JpaRepository<ScoreCard, Long> {
}