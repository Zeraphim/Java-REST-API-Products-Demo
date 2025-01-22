package com.g4.RestApiScorecardsDemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.g4.RestApiScorecardsDemo.dto.ScoreCardDTO;
import com.g4.RestApiScorecardsDemo.service.ScoreCardService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/scorecards")
public class ScoreCardControllerV1 {

    @Autowired
    private ScoreCardService scorecardService;

    // Get all products
    @GetMapping
    public ResponseEntity<List<ScoreCardDTO>> getAllScorecard() {
        List<ScoreCardDTO> scorecards = scorecardService.getAllScorecard();
        return ResponseEntity.ok(scorecards);
    }

    // Get a product by ID
    @GetMapping("/{id}")
    public ResponseEntity<ScoreCardDTO> getScorecardById(@PathVariable Long id) {
        ScoreCardDTO scorecard = scorecardService.getScorecardById(id);
        return ResponseEntity.ok(scorecard);
    }

    // Create a new product
    // PRONE TO SQL INJECTION
    @PostMapping
    public ResponseEntity<ScoreCardDTO> createScorecard(@RequestBody ScoreCardDTO ScoreCardDTO) {
        ScoreCardDTO createdScoreCard = scorecardService.createScorecard(ScoreCardDTO);
        return ResponseEntity.ok(createdScoreCard);
    }

    // Update a product
    @PutMapping("/{id}")
    public ResponseEntity<ScoreCardDTO> updateScorecard(@PathVariable Long id, @RequestBody ScoreCardDTO ScoreCardDTO) {
        ScoreCardDTO updatedProduct = scorecardService.updateScoreCard(id, ScoreCardDTO);
        return ResponseEntity.ok(updatedProduct);
    }

    // Delete a product
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScorecard(@PathVariable Long id) {
        scorecardService.deleteScorecard(id);
        return ResponseEntity.noContent().build();
    }
}