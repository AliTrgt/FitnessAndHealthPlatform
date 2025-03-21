package com.example.HealthAndFitnessPlatform.controller;

import com.example.HealthAndFitnessPlatform.dto.RecipeDTO;
import com.example.HealthAndFitnessPlatform.model.Recipe;
import com.example.HealthAndFitnessPlatform.service.IngredientService;
import com.example.HealthAndFitnessPlatform.service.RecipeService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/recipe")
@Slf4j
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping
    public ResponseEntity<List<RecipeDTO>> getAllRecipe(){
             List<RecipeDTO> recipeDto =  recipeService.getAllRecipes();
             return new ResponseEntity<>(recipeDto,HttpStatus.OK);
    }

    @GetMapping("/{recipeId}")
    public ResponseEntity<RecipeDTO> findById(@PathVariable int recipeId){
            RecipeDTO recipeDTO = recipeService.findById(recipeId);
            return new ResponseEntity<>(recipeDTO,HttpStatus.OK);
    }

    @GetMapping("/find/{userId}")
    public ResponseEntity<List<RecipeDTO>> getRecipesByUserId(@PathVariable int userId){
            List<RecipeDTO> recipeList  = recipeService.getRecipesByUserId(userId);
            return new ResponseEntity<>(recipeList,HttpStatus.OK);
    }


    @PostMapping("/create")
    public ResponseEntity<RecipeDTO> createRecipe(@Valid @RequestBody RecipeDTO recipe){
            RecipeDTO savedUser = recipeService.createRecipe(recipe);
            return new ResponseEntity<>(savedUser,HttpStatus.CREATED);
    }

    @PutMapping("/update/{recipeId}")
    public ResponseEntity<RecipeDTO> updateRecipe(@PathVariable int recipeId,@Valid @RequestBody RecipeDTO recipe){
           RecipeDTO recipeDTO =  recipeService.updateRecipe(recipeId,recipe);
            return new ResponseEntity<>(recipeDTO,HttpStatus.OK);
    }

    @DeleteMapping("/{recipeId}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable int recipeId){
                recipeService.deleteRecipe(recipeId);
                return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/rec/{userId}")
    public ResponseEntity<List<RecipeDTO>> getRecommendation(@PathVariable int userId){
             List<RecipeDTO> recipeList = recipeService.getRecommendations(userId);
             return new ResponseEntity<>(recipeList,HttpStatus.OK);
    }

}
