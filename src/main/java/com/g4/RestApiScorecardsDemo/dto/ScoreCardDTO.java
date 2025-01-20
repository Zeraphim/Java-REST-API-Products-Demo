package com.g4.RestApiScorecardsDemo.dto;

public class ScoreCardDTO {

    private Long id;
    private String name;
    private String description;
    private int score;

    // Constructors, getters, and setters

    public ScoreCardDTO() {}

    public ScoreCardDTO(Long id, String name, String description, int score) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.score = score;
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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}