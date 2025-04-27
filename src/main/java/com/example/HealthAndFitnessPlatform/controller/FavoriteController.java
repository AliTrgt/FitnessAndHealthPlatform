package com.example.HealthAndFitnessPlatform.controller;

import com.example.HealthAndFitnessPlatform.dto.FavoriteDTO;
import com.example.HealthAndFitnessPlatform.model.Favorite;
import com.example.HealthAndFitnessPlatform.service.FavoriteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/favorite")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping
    public ResponseEntity<List<FavoriteDTO>> getAllFavorite(){
            List<FavoriteDTO> favoriteList = favoriteService.getAllFavorite();
            return new ResponseEntity<>(favoriteList, HttpStatus.OK);
    }

    @PostMapping("/toggle")
    public ResponseEntity<FavoriteDTO> toggleFavorite(@RequestBody FavoriteDTO favoriteDTO){
            FavoriteDTO favorite = favoriteService.toggleFavorite(favoriteDTO.getUserId(),favoriteDTO.getRecipeId());
            return new ResponseEntity<>(favorite,HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<FavoriteDTO> createFavorite(@RequestBody FavoriteDTO favoriteDTO){
            FavoriteDTO favorite= favoriteService.createFavorite(favoriteDTO.getUserId(),favoriteDTO.getRecipeId());
            return new ResponseEntity<>(favorite,HttpStatus.CREATED);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteFavorite(@RequestBody FavoriteDTO favoriteDTO){
            favoriteService.deleteFavorite(favoriteDTO.getUserId(),favoriteDTO.getRecipeId());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<FavoriteDTO> findByUserId(@PathVariable int userId){
            FavoriteDTO favorite = favoriteService.findByUserId(userId);
            return new ResponseEntity<>(favorite,HttpStatus.OK);
    }


    @GetMapping("/list/{userId}")
    public ResponseEntity<List<FavoriteDTO>> findFavoriteById(@PathVariable int userId){
            List<FavoriteDTO> favorites = favoriteService.findFavoritesByUserId(userId);
            return new ResponseEntity<>(favorites,HttpStatus.OK);
    }

    @GetMapping("/isFavorite")
    public boolean existByUserIdAndRecipe(@RequestParam int userId,@RequestParam int recipeId){
            return favoriteService.existByUserIdAndRecipe(userId,recipeId);
    }

}
