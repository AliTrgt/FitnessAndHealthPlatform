package com.example.HealthAndFitnessPlatform.dto;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class RecipeDTO {

    private int id;
    private String title;
    private String description;
    private String instructions;
    private int prepTime;
    private double calories;
    private LocalDateTime createdAt;
    private int likeCount;
    private int userId;
    private List<IngredientDTO> ingredientList;
    private List<LikeDTO> likeList;
    private List<CommentDTO> commentList;


}
