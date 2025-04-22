package com.example.HealthAndFitnessPlatform.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    private int likeCount;
    private String imageUrl;
    private int userId;
    private List<IngredientDTO> ingredientList;
    private List<LikeDTO> likeList;
    private List<CommentDTO> commentList;


}
