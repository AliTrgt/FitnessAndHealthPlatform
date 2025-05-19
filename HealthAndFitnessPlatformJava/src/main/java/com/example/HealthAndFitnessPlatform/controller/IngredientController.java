package com.example.HealthAndFitnessPlatform.controller;

import com.example.HealthAndFitnessPlatform.dto.IngredientDTO;
import com.example.HealthAndFitnessPlatform.dto.RecipeDTO;
import com.example.HealthAndFitnessPlatform.dto.RecipeIngredientsUpdateDTO;
import com.example.HealthAndFitnessPlatform.exception.RecipeNotFoundException;
import com.example.HealthAndFitnessPlatform.model.Ingredient;
import com.example.HealthAndFitnessPlatform.model.Recipe;
import com.example.HealthAndFitnessPlatform.repository.IngredientRepository;
import com.example.HealthAndFitnessPlatform.repository.RecipeRepository;
import com.example.HealthAndFitnessPlatform.service.IngredientService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/v1/ingredient")
public class IngredientController {

    private final IngredientService ingredientService;
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final ModelMapper modelMapper;

    public IngredientController(IngredientService ingredientService, RecipeRepository recipeRepository, IngredientRepository ingredientRepository, ModelMapper modelMapper) {
        this.ingredientService = ingredientService;
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<List<IngredientDTO>> getAllIngredients(){
            List<IngredientDTO> ingredientList = ingredientService.getAllIngredient();
            return new ResponseEntity<>(ingredientList, HttpStatus.OK);
    }

    @GetMapping("/{ingredientId}")
    public ResponseEntity<IngredientDTO> findById(@PathVariable int ingredientId){
            IngredientDTO ingredient = ingredientService.findById(ingredientId);
            return new ResponseEntity<>(ingredient,HttpStatus.OK);
    }

    @PostMapping("/create/{recipeId}")
    public ResponseEntity<IngredientDTO> createIngredient(@PathVariable int recipeId,@Valid @RequestBody IngredientDTO ingredientDTO){
            IngredientDTO ingredient = ingredientService.createIngredient(recipeId,ingredientDTO);
            return new ResponseEntity<>(ingredient,HttpStatus.CREATED);
    }

    @PutMapping("/update/{ingredientId}")
    public ResponseEntity<IngredientDTO> updateIngredient(@PathVariable int ingredientId,@Valid @RequestBody IngredientDTO ingredientDTO){
            IngredientDTO ingredient = ingredientService.updateIngredient(ingredientId,ingredientDTO);
            return new ResponseEntity<>(ingredient,HttpStatus.OK);
    }

    @DeleteMapping("/{ingredientId}")
    public ResponseEntity<Void> deleteIngredient(@PathVariable int ingredientId){
                ingredientService.deleteIngredient(ingredientId);
                return  ResponseEntity.noContent().build();
    }


    @PatchMapping("/recipes/ingredients/batch")
    @Transactional
    public ResponseEntity<List<RecipeDTO>> updateIngredientsBatch(@RequestBody List<RecipeIngredientsUpdateDTO> updateDTOList) {
        List<RecipeDTO> updatedRecipes = new ArrayList<>();

        for (RecipeIngredientsUpdateDTO updateDTO : updateDTOList) {
            Recipe recipe = recipeRepository.findById(updateDTO.getRecipeId())
                    .orElseThrow(() -> new RecipeNotFoundException("Recipe not found: " + updateDTO.getRecipeId()));

            List<Ingredient> updatedIngredients = new ArrayList<>();

            for (IngredientDTO dto : updateDTO.getIngredients()) {
                Ingredient ingredient;

                if (dto.getId() > 0) {
                    ingredient = ingredientRepository.findById(dto.getId())
                            .orElseGet(() -> {
                                Ingredient newIngredient = new Ingredient();
                                newIngredient.setName(dto.getName());
                                newIngredient.setQuantity(dto.getQuantity());
                                return ingredientRepository.save(newIngredient);
                            });
                } else {
                    ingredient = new Ingredient();
                    ingredient.setName(dto.getName());
                    ingredient.setQuantity(dto.getQuantity());
                    ingredient = ingredientRepository.save(ingredient);
                }

                updatedIngredients.add(ingredient);
            }

            recipe.setIngredientList(updatedIngredients);
            Recipe savedRecipe = recipeRepository.save(recipe);
            updatedRecipes.add(modelMapper.map(savedRecipe, RecipeDTO.class));
        }

        return ResponseEntity.ok(updatedRecipes);
    }


}
