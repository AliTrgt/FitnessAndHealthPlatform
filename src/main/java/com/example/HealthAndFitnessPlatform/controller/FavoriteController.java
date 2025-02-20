package com.example.HealthAndFitnessPlatform.controller;

import com.example.HealthAndFitnessPlatform.dto.FavoriteDTO;
import com.example.HealthAndFitnessPlatform.model.Favorite;
import com.example.HealthAndFitnessPlatform.service.FavoriteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/toggle/{userId}/{recipeId}")
    public ResponseEntity<FavoriteDTO> toggleFavorite(@PathVariable int userId, @PathVariable int recipeId){
            FavoriteDTO favorite = favoriteService.toggleFavorite(userId,recipeId);
            return new ResponseEntity<>(favorite,HttpStatus.OK);
    }

    @PostMapping("/create/{userId}/{recipeId}")
    public ResponseEntity<FavoriteDTO> createFavorite(@PathVariable int userId,@PathVariable int recipeId){
            FavoriteDTO favoriteDTO = favoriteService.createFavorite(userId,recipeId);
            return new ResponseEntity<>(favoriteDTO,HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{userId}/{recipeId}")
    public ResponseEntity<Void> deleteFavorite(@PathVariable int userId,@PathVariable int recipeId){
            favoriteService.deleteFavorite(userId,recipeId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
