package com.example.HealthAndFitnessPlatform.service;

import com.example.HealthAndFitnessPlatform.dto.DTOConverter;
import com.example.HealthAndFitnessPlatform.dto.RecipeDTO;
import com.example.HealthAndFitnessPlatform.exception.RecipeNotFoundException;
import com.example.HealthAndFitnessPlatform.model.Recipe;
import com.example.HealthAndFitnessPlatform.repository.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final DTOConverter dtoConverter;

    public RecipeService(RecipeRepository recipeRepository, DTOConverter dtoConverter) {
        this.recipeRepository = recipeRepository;
        this.dtoConverter = dtoConverter;
    }

    public List<RecipeDTO> getAllRecipes(){
           return recipeRepository.findAll()
                    .stream()
                    .map(dtoConverter::convertToRecipeDTO)
                    .collect(Collectors.toList());
    }

    public RecipeDTO findById(int recipeId){
           Recipe recipe =  recipeRepository.findById(recipeId).orElseThrow(() -> new RecipeNotFoundException("Recipe id not found : "+recipeId));
           return dtoConverter.convertToRecipeDTO(recipe);
    }

    public RecipeDTO createRecipe(Recipe recipe){
            Recipe lastRecipe = recipeRepository.save(recipe);
            return dtoConverter.convertToRecipeDTO(lastRecipe);
    }

    public RecipeDTO updateRecipe(int recipeId,Recipe recipe){
            Recipe firstRecipe = recipeRepository.findById(recipeId).orElseThrow(() -> new RecipeNotFoundException("Recipe Id Not Found : "+recipeId));
            firstRecipe.setCalories(recipe.getCalories());
            firstRecipe.setDescription(recipe.getDescription());
            firstRecipe.setInstructions(recipe.getInstructions());
            firstRecipe.setTitle(recipe.getTitle());
            firstRecipe.setPrepTime(recipe.getPrepTime());
            firstRecipe.setLikeCount(recipe.getLikeCount());

            Recipe lastRecipe = recipeRepository.save(firstRecipe);

            return dtoConverter.convertToRecipeDTO(lastRecipe);
    }

    public void deleteRecipe(int recipeId){
            Recipe lastRecipe = recipeRepository.findById(recipeId).orElseThrow(() -> new RecipeNotFoundException("Recipe Id Not Found : "+recipeId));
            recipeRepository.delete(lastRecipe);
    }

}
