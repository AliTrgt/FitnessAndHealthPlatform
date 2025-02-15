package com.example.HealthAndFitnessPlatform.dto;

import com.example.HealthAndFitnessPlatform.model.*;

import java.util.stream.Collectors;

public class DTOConverter {


    public UserDTO convertToUserDTO(User user){
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getProfilePhoto(),
                user.getCreatedAt(),
                user.getRecipeList().stream().map(this::convertToRecipeDTO).toList(),
                user.getLikeList().stream().map(this::convertToLikeDTO).toList(),
                user.getCommentList().stream().map(this::convertToCommentDTO).toList(),
                user.getFavoriteList().stream().map(this::convertToFavoriteDTO).toList(),
                user.getFollowers().stream().map(this::convertToFallowDTO).toList(),
                user.getFollowing().stream().map(this::convertToFallowDTO).toList()
        );
    }


    public FollowDTO convertToFallowDTO(Follow follow){
        return new FollowDTO(follow.getId(),
                follow.getFollower().getId(),
                follow.getFollowing().getId());
    }

    public FavoriteDTO convertToFavoriteDTO(Favorite favorite) {
        return new FavoriteDTO(favorite.getId(),
                favorite.getUser().getId(),
                favorite.getRecipe().getId(),
                favorite.getCreatedAt());
    }


    public CommentDTO convertToCommentDTO(Comment comment) {
        return new CommentDTO(comment.getId(),
                comment.getUser().getId(),
                comment.getRecipe().getId(),
                comment.getContent(),
                comment.getCreatedAt());
    }

    public LikeDTO convertToLikeDTO(Like like) {
        return new LikeDTO(like.getId(),
                like.getUser().getId(),
                like.getRecipe().getId(),
                like.getCreatedAt());
    }

    public RecipeDTO convertToRecipeDTO(Recipe recipe) {
        return new RecipeDTO(recipe.getId(),
                recipe.getTitle(),
                recipe.getDescription(),
                recipe.getInstructions(),
                recipe.getPrepTime(),
                recipe.getCalories(),
                recipe.getCreatedAt(),
                recipe.getLikeCount(),
                recipe.getUser().getId(),
                recipe.getIngredientList().stream().map(this::convertToIngredientDTO).collect(Collectors.toList()),
                recipe.getLikeList().stream().map(this::convertToLikeDTO).collect(Collectors.toList()),
                recipe.getCommentList().stream().map(this::convertToCommentDTO).collect(Collectors.toList())
        );
    }

    public IngredientDTO convertToIngredientDTO(Ingredient ingredient){
        return new IngredientDTO(ingredient.getId(),
                ingredient.getName()
        );
    }
}

