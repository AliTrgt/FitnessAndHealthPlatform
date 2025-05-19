package com.example.HealthAndFitnessPlatform.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RecipeIngredientsUpdateDTO {
    private int recipeId;
    private List<IngredientDTO> ingredients;
}
