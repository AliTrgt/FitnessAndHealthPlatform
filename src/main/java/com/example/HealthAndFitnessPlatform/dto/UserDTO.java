package com.example.HealthAndFitnessPlatform.dto;

import com.example.HealthAndFitnessPlatform.model.Role;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

public record UserDTO(int id,
                      String username,
                      List<Role> authorities,
                      String email,
                      String profilePhoto,
                      double height,
                      double weight,
                      LocalDateTime createdAt,
                      List<RecipeDTO> recipeList,
                      List<LikeDTO> likeList,
                      List<CommentDTO> commentList,
                      List<FavoriteDTO> favoriteList,
                      List<FollowDTO> followers,
                      List<FollowDTO> following) {
}
