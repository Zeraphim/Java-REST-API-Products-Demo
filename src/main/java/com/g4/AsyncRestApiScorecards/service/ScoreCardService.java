package com.g4.AsyncRestApiScorecards.service;

import com.g4.AsyncRestApiScorecards.dto.ScoreCardDTO;
import com.g4.AsyncRestApiScorecards.entity.ScoreCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScoreCardService {

    @Autowired
    private com.g4.AsyncRestApiScorecards.repository.ScoreCardRepository ScoreCardRepository;

    // Convert Product entity to ScoreCardDTO
    private ScoreCardDTO mapToDTO(ScoreCard scorecard) {
        return new ScoreCardDTO(scorecard.getId(), scorecard.getName(), scorecard.getDescription(), scorecard.getScore());
    }

    // Convert ScoreCardDTO to Scorecard entity
    private ScoreCard mapToEntity(ScoreCardDTO ScoreCardDTO) {
        return new ScoreCard(ScoreCardDTO.getName(), ScoreCardDTO.getDescription(), ScoreCardDTO.getScore());
    }

    // Get all scorecard
    public List<ScoreCardDTO> getAllScorecard() {
        return ScoreCardRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Get a single scorecard by ID
    public ScoreCardDTO getScorecardById(Long id) {
        ScoreCard scorecard = ScoreCardRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        return mapToDTO(scorecard);
    }

    // Create a new scorecard
    public ScoreCardDTO createScorecard(ScoreCardDTO ScoreCardDTO) {
        ScoreCard scorecard = mapToEntity(ScoreCardDTO);
        ScoreCard savedscorecard = ScoreCardRepository.save(scorecard);
        return mapToDTO(savedscorecard);
    }

    // Update an existing scorecard
    public ScoreCardDTO updateScoreCard(Long id, ScoreCardDTO ScoreCardDTO) {
        ScoreCard existingScoreCard = ScoreCardRepository.findById(id).orElseThrow(() -> new RuntimeException("Scorecard not found"));
        existingScoreCard.setName(ScoreCardDTO.getName());
        existingScoreCard.setDescription(ScoreCardDTO.getDescription());
        existingScoreCard.setScore(ScoreCardDTO.getScore());
        ScoreCard updatedScoreCard = ScoreCardRepository.save(existingScoreCard);
        return mapToDTO(updatedScoreCard);
    }

    // Delete a scorecard
    public void deleteScorecard(Long id) {
        ScoreCard scorecard = ScoreCardRepository.findById(id).orElseThrow(() -> new RuntimeException("Scorecard not found"));
        ScoreCardRepository.delete(scorecard);
    }
}
