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

    @PostMapping("/toggle")
    public ResponseEntity<FavoriteDTO> toggleFavorite(@RequestParam int userId, @RequestParam int recipeId){
            FavoriteDTO favorite = favoriteService.toggleFavorite(userId,recipeId);
            return new ResponseEntity<>(favorite,HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<FavoriteDTO> createFavorite(@RequestParam int userId,@RequestParam int recipeId){
            FavoriteDTO favoriteDTO = favoriteService.createFavorite(userId,recipeId);
            return new ResponseEntity<>(favoriteDTO,HttpStatus.CREATED);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteFavorite(@RequestParam int userId,@RequestParam int recipeId){
            favoriteService.deleteFavorite(userId,recipeId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
