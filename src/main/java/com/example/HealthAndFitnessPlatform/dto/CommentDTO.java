package com.example.HealthAndFitnessPlatform.dto;

import java.time.LocalDateTime;

public record CommentDTO(int id,
                         int userId,
                         int recipeId,
                         String content,
                         LocalDateTime createdAt) {
}
