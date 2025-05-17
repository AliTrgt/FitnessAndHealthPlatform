package com.example.HealthAndFitnessPlatform.controller;

import com.example.HealthAndFitnessPlatform.dto.RecipeDTO;
import com.example.HealthAndFitnessPlatform.model.Recipe;
import com.example.HealthAndFitnessPlatform.repository.RecipeRepository;
import com.example.HealthAndFitnessPlatform.service.IngredientService;
import com.example.HealthAndFitnessPlatform.service.RecipeService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/v1/recipe")
@Slf4j
public class RecipeController {

    private final RecipeService recipeService;
    private final ImageUploadController imageUploadController;
    private final RecipeRepository recipeRepository;
    private final ModelMapper modelMapper;

    public RecipeController(RecipeService recipeService, ImageUploadController imageUploadController, RecipeRepository recipeRepository, ModelMapper modelMapper) {
        this.recipeService = recipeService;
        this.imageUploadController = imageUploadController;
        this.recipeRepository = recipeRepository;
        this.modelMapper = modelMapper;
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
    @Transactional
    public ResponseEntity<List<RecipeDTO>> getRecommendation(@PathVariable int userId){
             List<RecipeDTO> recipeList = recipeService.getRecommendations(userId);
             return new ResponseEntity<>(recipeList,HttpStatus.OK);
    }


    @PostMapping("/upload/{recipeId}")
    public ResponseEntity<String> uploadRecipeImages(@PathVariable int recipeId, @RequestParam("file") MultipartFile file) throws IOException {

            String filePath = imageUploadController.saveImage(file);

            recipeService.updateRecipeImage(recipeId,filePath);

            return ResponseEntity.ok("Recipe Photo Uploaded : "+filePath);

    }

    @PostMapping("/bulk")
    public ResponseEntity<?> uploadBulkRecipe(@RequestBody List<RecipeDTO> recipes){
                recipes.forEach(recipeService::createRecipe);
                return ResponseEntity.ok("Recipes uploaded successfully");
    }


    @GetMapping("/get10")
    public Page<RecipeDTO> getRecipes(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size) {
        return recipeRepository.findAll(PageRequest.of(page, size))
                .map(recipe -> modelMapper.map(recipe,RecipeDTO.class));
    }


}
