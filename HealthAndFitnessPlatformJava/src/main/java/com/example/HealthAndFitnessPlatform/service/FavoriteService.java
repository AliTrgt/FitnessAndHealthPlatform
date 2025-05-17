package com.example.HealthAndFitnessPlatform.service;

import com.example.HealthAndFitnessPlatform.dto.FavoriteDTO;
import com.example.HealthAndFitnessPlatform.exception.FavoriteNotFoundException;
import com.example.HealthAndFitnessPlatform.exception.RecipeNotFoundException;
import com.example.HealthAndFitnessPlatform.exception.UserNotFoundException;
import com.example.HealthAndFitnessPlatform.model.Favorite;
import com.example.HealthAndFitnessPlatform.model.Recipe;
import com.example.HealthAndFitnessPlatform.model.User;
import com.example.HealthAndFitnessPlatform.repository.FavoriteRepository;
import com.example.HealthAndFitnessPlatform.repository.RecipeRepository;
import com.example.HealthAndFitnessPlatform.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;

    public FavoriteService(FavoriteRepository favoriteRepository,ModelMapper modelMapper, UserRepository userRepository, RecipeRepository recipeRepository) {
        this.favoriteRepository = favoriteRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
    }

    public List<FavoriteDTO> getAllFavorite(){
        List<Favorite> favoriteList = favoriteRepository.findAll();
        return favoriteList.isEmpty() ? Collections.emptyList() : favoriteList
                    .stream()
                    .map(favorite -> modelMapper.map(favorite,FavoriteDTO.class))
                    .collect(Collectors.toList());
    }

    @Transactional
    public FavoriteDTO createFavorite(int userId, int recipeId){
        User user  = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("userId not found !! : "+userId));
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new RecipeNotFoundException("recipe not found !! : "+recipeId));

        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setRecipe(recipe);
        favorite.setCreatedAt(LocalDateTime.now());
        Favorite savedFavorite  = favoriteRepository.save(favorite);
          return modelMapper.map(savedFavorite,FavoriteDTO.class);
    }

    @Transactional
    public void deleteFavorite(int userId, int recipeId) {
        Favorite favorite = favoriteRepository.findByUserIdAndRecipeId(userId, recipeId)
                .orElseThrow(() -> new FavoriteNotFoundException("Favorite not found for userId: " + userId + " and recipeId: " + recipeId));

        favoriteRepository.delete(favorite);
    }


    @Transactional
    public FavoriteDTO toggleFavorite(int userId,int recipeId){
            User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found : "+userId));
            Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new RecipeNotFoundException("Recipe not found : "+recipeId));

            Optional<Favorite> favorite  = favoriteRepository.findByUserIdAndRecipeId(userId,recipeId);

            if (favorite.isPresent()){
                    favoriteRepository.delete(favorite.get());
                    return null;
            }

            else {
                    Favorite savedFavorite = new Favorite();
                    savedFavorite.setUser(user);
                    savedFavorite.setRecipe(recipe);
                    savedFavorite.setCreatedAt(LocalDateTime.now());
                    Favorite lastFavorite = favoriteRepository.save(savedFavorite);
                    return modelMapper.map(lastFavorite,FavoriteDTO.class);
            }

    }

    public FavoriteDTO findByUserId(int userId){
            Favorite favorite = favoriteRepository.findByUserId(userId);
            return modelMapper.map(favorite,FavoriteDTO.class);
    }


    public List<FavoriteDTO> findFavoritesByUserId(int userId){
            List<Favorite> favorites =  favoriteRepository.findFavoritesByUserId(userId);
            return favorites
                    .stream()
                    .map(favorite -> modelMapper.map(favorite,FavoriteDTO.class))
                    .collect(Collectors.toList());
    }

    public boolean existByUserIdAndRecipe(int userId,int recipeId){
            return favoriteRepository.existsByUserIdAndRecipeId(userId,recipeId);
    }

}
