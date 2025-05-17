package com.example.HealthAndFitnessPlatform.service;

import com.example.HealthAndFitnessPlatform.dto.IngredientDTO;
import com.example.HealthAndFitnessPlatform.exception.IngredientNotFoundException;
import com.example.HealthAndFitnessPlatform.model.Ingredient;
import com.example.HealthAndFitnessPlatform.model.Recipe;
import com.example.HealthAndFitnessPlatform.repository.IngredientRepository;
import com.example.HealthAndFitnessPlatform.repository.RecipeRepository;
import jakarta.transaction.Transactional;
import org.antlr.v4.runtime.atn.LexerATNSimulator;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IngredientService {

    private final ModelMapper modelMapper;
    private final IngredientRepository ingredientRepository;
    private final RecipeRepository recipeRepository;

    public IngredientService(ModelMapper modelMapper, IngredientRepository ingredientRepository, RecipeRepository recipeRepository) {
        this.modelMapper = modelMapper;
        this.ingredientRepository = ingredientRepository;
        this.recipeRepository = recipeRepository;
    }

    public List<IngredientDTO> getAllIngredient(){
        List<Ingredient> ingredientList = ingredientRepository.findAll();
        return  ingredientList.isEmpty() ? Collections.emptyList() : ingredientList
                    .stream()
                    .map(ingredient -> modelMapper.map(ingredient,IngredientDTO.class))
                    .collect(Collectors.toList());
    }

    public IngredientDTO findById(int ingredientId){
        Ingredient ingredient = ingredientRepository.findById(ingredientId).orElseThrow(() -> new IngredientNotFoundException("Ingredient not found : "+ingredientId));
        return modelMapper.map(ingredient,IngredientDTO.class);
    }

    public IngredientDTO createIngredient(int recipeId,IngredientDTO ingredientDto){
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe : "+recipeId));

        Ingredient lastIng = modelMapper.map(ingredientDto, Ingredient.class);
        Ingredient saved = ingredientRepository.save(lastIng);

        recipe.getIngredientList().add(saved);
        recipeRepository.save(recipe);
        return modelMapper.map(saved,IngredientDTO.class);

    }

    @Transactional
    public IngredientDTO updateIngredient(int ingredientId,IngredientDTO ingredient){
        Ingredient firstIngredient = ingredientRepository.findById(ingredientId).orElseThrow(() -> new IngredientNotFoundException("Ingredient not found : "+ingredientId));
        firstIngredient.setName(ingredient.getName());
        firstIngredient.setQuantity(ingredient.getQuantity());
        Ingredient lastIngredient = ingredientRepository.save(firstIngredient);
        return modelMapper.map(lastIngredient,IngredientDTO.class);
    }

    @Transactional
    public void deleteIngredient(int ingredientId) {
        Ingredient ing = ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new IngredientNotFoundException("ingredientId : "+ingredientId));

        // 1) Her ilişkili recipe'den ingredient'i çıkar
        for (Recipe r : ing.getRecipeList()) {
            r.getIngredientList().remove(ing);
        }
        recipeRepository.saveAll(ing.getRecipeList());

        // 3) Şimdi ingredient'ı sil
        ingredientRepository.delete(ing);
    }


}
