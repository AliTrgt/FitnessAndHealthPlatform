package com.example.HealthAndFitnessPlatform.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FavoriteDTO {

    private int id;
    private int userId;
    private int recipeId;
    private LocalDateTime createdAt;

}
