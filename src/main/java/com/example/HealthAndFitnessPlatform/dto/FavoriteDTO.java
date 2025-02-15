package com.example.HealthAndFitnessPlatform.dto;

import java.time.LocalDateTime;

public record FavoriteDTO(int id,
                          int userId,
                          int recipeId,
                          LocalDateTime createdAt) {
}
