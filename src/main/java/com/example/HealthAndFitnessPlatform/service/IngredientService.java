package com.example.HealthAndFitnessPlatform.service;

import com.example.HealthAndFitnessPlatform.dto.DTOConverter;
import com.example.HealthAndFitnessPlatform.dto.IngredientDTO;
import com.example.HealthAndFitnessPlatform.exception.IngredientNotFoundException;
import com.example.HealthAndFitnessPlatform.model.Ingredient;
import com.example.HealthAndFitnessPlatform.repository.IngredientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IngredientService {

    private final DTOConverter dtoConverter;
    private final IngredientRepository ingredientRepository;

    public IngredientService(DTOConverter dtoConverter, IngredientRepository ingredientRepository) {
        this.dtoConverter = dtoConverter;
        this.ingredientRepository = ingredientRepository;
    }

    public List<IngredientDTO> getAllIngredient(){
          return  ingredientRepository.findAll()
                    .stream()
                    .map(dtoConverter::convertToIngredientDTO)
                    .collect(Collectors.toList());
    }

    public IngredientDTO findById(int ingredientId){
        Ingredient ingredient = ingredientRepository.findById(ingredientId).orElseThrow(() -> new IngredientNotFoundException("Ingredient not found : "+ingredientId));
        return dtoConverter.convertToIngredientDTO(ingredient);
    }

    public IngredientDTO createIngredient(Ingredient ingredient){
           Ingredient lastIngredient = ingredientRepository.save(ingredient);
           return dtoConverter.convertToIngredientDTO(lastIngredient);
    }

    public IngredientDTO updateIngredient(int ingredientId,Ingredient ingredient){
        Ingredient firstIngredient = ingredientRepository.findById(ingredientId).orElseThrow(() -> new IngredientNotFoundException("Ingredient not found : "+ingredientId));
        firstIngredient.setName(ingredient.getName());

        Ingredient lastIngredient = ingredientRepository.save(firstIngredient);
        return dtoConverter.convertToIngredientDTO(lastIngredient);
    }

    public void deleteIngredient(int ingredientId){
            Ingredient ingredient = ingredientRepository.findById(ingredientId).orElseThrow(() -> new IngredientNotFoundException("Ingredient not found : "+ingredientId));
            ingredientRepository.delete(ingredient);
    }


}
