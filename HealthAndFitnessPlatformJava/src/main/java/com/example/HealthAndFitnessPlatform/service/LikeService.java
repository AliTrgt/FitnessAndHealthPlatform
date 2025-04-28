package com.example.HealthAndFitnessPlatform.service;

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
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;

    public LikeService(LikeRepository likeRepository,ModelMapper modelMapper, UserRepository userRepository, RecipeRepository recipeRepository) {
        this.likeRepository = likeRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
    }

    public List<LikeDTO> getAllLike(){
            List<Like> likeLike = likeRepository.findAll();
            return likeLike.isEmpty() ? Collections.emptyList() : likeLike
                    .stream()
                    .map(like -> modelMapper.map(like,LikeDTO.class))
                    .collect(Collectors.toList());
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
            Like lastLike = likeRepository.save(like);
            return   modelMapper.map(lastLike,LikeDTO.class);
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
            return modelMapper.map(savedLike,LikeDTO.class);
        }
    }

    public LikeDTO findLikeByUserId(int userId){
            Like like = likeRepository.findLikeByUserId(userId);
            return modelMapper.map(like,LikeDTO.class);
    }

    public List<LikeDTO> findLikesByUserId(int userId){
            List<Like> likes = likeRepository.findLikesByUserId(userId);
            return likes
                    .stream()
                    .map(like -> modelMapper.map(like,LikeDTO.class))
                    .collect(Collectors.toList());
    }

}
