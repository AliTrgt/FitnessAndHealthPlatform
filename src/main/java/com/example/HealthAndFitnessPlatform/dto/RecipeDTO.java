package com.example.HealthAndFitnessPlatform.dto;

import java.time.LocalDateTime;
import java.util.List;

public record RecipeDTO(int id,
                        String title,
                        String description,
                        String instructions,
                        int prepTime,
                        double calories,
                        LocalDateTime createdAt,
                        int likeCount,
                        int userId,
                        List<IngredientDTO> ingredientList,
                        List<LikeDTO> likeList,
                        List<CommentDTO> commentList) {
}
