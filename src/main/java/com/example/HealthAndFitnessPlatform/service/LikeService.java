package com.example.HealthAndFitnessPlatform.service;

import com.example.HealthAndFitnessPlatform.dto.DTOConverter;
import com.example.HealthAndFitnessPlatform.dto.LikeDTO;
import com.example.HealthAndFitnessPlatform.exception.LikeNotFoundException;
import com.example.HealthAndFitnessPlatform.exception.RecipeNotFoundException;
import com.example.HealthAndFitnessPlatform.exception.UserNotFoundException;
import com.example.HealthAndFitnessPlatform.model.Like;
import com.example.HealthAndFitnessPlatform.model.Recipe;
import com.example.HealthAndFitnessPlatform.model.User;
import com.example.HealthAndFitnessPlatform.repository.LikeRepository;
import com.example.HealthAndFitnessPlatform.repository.RecipeRepository;
import com.example.HealthAndFitnessPlatform.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final DTOConverter dtoConverter;
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;

    public LikeService(LikeRepository likeRepository, DTOConverter dtoConverter, UserRepository userRepository, RecipeRepository recipeRepository) {
        this.likeRepository = likeRepository;
        this.dtoConverter = dtoConverter;
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
    }

    @Transactional
    public LikeDTO createLike(int userId,int recipeId){
            User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found : "+userId));
            Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new RecipeNotFoundException("Recipe not found : "+recipeId));

            if(likeRepository.existsLikeByUserIdAndRecipeId(userId,recipeId)){
                    throw new RuntimeException("You already like this post");
            }

            Like like = new Like();
            like.setUser(user);
            like.setRecipe(recipe);
            like.setCreatedAt(LocalDateTime.now());

            recipe.setLikeCount(recipe.getLikeCount() + 1);
            recipeRepository.save(recipe);

         return   dtoConverter.convertToLikeDTO(likeRepository.save(like));
    }

    @Transactional
    public void deleteLike(int userId,int recipeId){
            Like like = likeRepository.findByUserIdAndRecipeId(userId,recipeId)
                    .orElseThrow(() -> new LikeNotFoundException("Like not found for userId: " + userId + " and recipeId: " + recipeId));
            Recipe recipe = like.getRecipe();

            recipe.setLikeCount(Math.max(recipe.getLikeCount() - 1, 0));
            recipeRepository.save(recipe);

            likeRepository.delete(like);
    }



    @Transactional
    public LikeDTO toggleLike(int userId,int recipeId){
           User user = userRepository.findById(userId).orElseThrow(() ->new UserNotFoundException("User not found : "+userId));
           Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new RecipeNotFoundException("Recipe not found : "+recipeId));

          Optional<Like> existLike = likeRepository.findByUserIdAndRecipeId(userId,recipeId);

        if (existLike.isPresent()){
                 likeRepository.delete(existLike.get());
                 recipeRepository.decrementLikeCount(recipeId);
                 return null;
          }
        else  {
            Like like = new Like();
            like.setUser(user);
            like.setRecipe(recipe);
            like.setCreatedAt(LocalDateTime.now());

            Like savedLike = likeRepository.save(like);
            recipeRepository.incrementLikeCount(recipeId);
            return dtoConverter.convertToLikeDTO(savedLike);
        }
    }

}
