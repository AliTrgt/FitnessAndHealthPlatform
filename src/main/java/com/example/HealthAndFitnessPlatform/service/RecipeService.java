package com.example.HealthAndFitnessPlatform.service;

import com.example.HealthAndFitnessPlatform.dto.RecipeDTO;
import com.example.HealthAndFitnessPlatform.exception.RecipeNotFoundException;
import com.example.HealthAndFitnessPlatform.model.Recipe;
import com.example.HealthAndFitnessPlatform.repository.RecipeRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final ModelMapper modelMapper;

    public RecipeService(RecipeRepository recipeRepository,ModelMapper modelMapper) {
        this.recipeRepository = recipeRepository;
        this.modelMapper = modelMapper;
    }

    public List<RecipeDTO> getAllRecipes(){
        List<Recipe> recipeList = recipeRepository.findAll();
        return  recipeList.isEmpty() ? Collections.emptyList() : recipeList
                    .stream()
                    .map(recipe -> modelMapper.map(recipe,RecipeDTO.class))
                    .collect(Collectors.toList());
    }

    public RecipeDTO findById(int recipeId){
           Recipe recipe =  recipeRepository.findById(recipeId).orElseThrow(() -> new RecipeNotFoundException("Recipe id not found : "+recipeId));
           return modelMapper.map(recipe,RecipeDTO.class);
    }

    public RecipeDTO createRecipe(RecipeDTO recipe){
            Recipe initialRecipe = modelMapper.map(recipe,Recipe.class);
            Recipe lastRecipe = recipeRepository.save(initialRecipe);
            return modelMapper.map(lastRecipe,RecipeDTO.class);
    }

    @Transactional
    public RecipeDTO updateRecipe(int recipeId,RecipeDTO recipe){
            Recipe firstRecipe = recipeRepository.findById(recipeId).orElseThrow(() -> new RecipeNotFoundException("Recipe Id Not Found : "+recipeId));
            firstRecipe.setCalories(recipe.getCalories());
            firstRecipe.setDescription(recipe.getDescription());
            firstRecipe.setInstructions(recipe.getInstructions());
            firstRecipe.setTitle(recipe.getTitle());
            firstRecipe.setPrepTime(recipe.getPrepTime());
            firstRecipe.setLikeCount(recipe.getLikeCount());

            Recipe lastRecipe = recipeRepository.save(firstRecipe);

            return modelMapper.map(lastRecipe,RecipeDTO.class);
    }

    public void deleteRecipe(int recipeId){
            Recipe lastRecipe = recipeRepository.findById(recipeId).orElseThrow(() -> new RecipeNotFoundException("Recipe Id Not Found : "+recipeId));
            recipeRepository.delete(lastRecipe);
    }

}
