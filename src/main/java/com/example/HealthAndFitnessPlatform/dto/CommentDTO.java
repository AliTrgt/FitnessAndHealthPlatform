package com.example.HealthAndFitnessPlatform.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDTO {

    private int id;
    private int userId;
    private int recipeId;
    private String content;
    private LocalDateTime createdAt;

}
