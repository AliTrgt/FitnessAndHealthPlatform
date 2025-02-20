package com.example.HealthAndFitnessPlatform.controller;

import com.example.HealthAndFitnessPlatform.dto.IngredientDTO;
import com.example.HealthAndFitnessPlatform.model.Ingredient;
import com.example.HealthAndFitnessPlatform.service.IngredientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/ingredient")
public class IngredientController {

    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
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

    @PostMapping("/create")
    public ResponseEntity<IngredientDTO> createIngredient(@Valid @RequestBody IngredientDTO ingredientDTO){
            IngredientDTO ingredient = ingredientService.createIngredient(ingredientDTO);
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
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
