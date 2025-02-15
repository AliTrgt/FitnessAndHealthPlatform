package com.example.HealthAndFitnessPlatform.dto;

import java.time.LocalDateTime;

public record LikeDTO(int id,
                      int userId,
                      int recipeId,
                      LocalDateTime createdAt) {
}
