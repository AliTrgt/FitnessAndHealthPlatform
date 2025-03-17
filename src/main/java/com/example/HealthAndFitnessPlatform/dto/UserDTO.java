package com.example.HealthAndFitnessPlatform.dto;

import com.example.HealthAndFitnessPlatform.model.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserDTO {

    private int id;
    private String username;
    private String password;
    private List<Role> authorities;
    private String email;
    private String profilePhoto;
    private double height;
    private double weight;
    private String gender;
    private int age;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    private  List<RecipeDTO> recipeList;
    private List<LikeDTO> likeList;
    private List<CommentDTO> commentList;
    private  List<FavoriteDTO> favoriteList;
    private  List<FollowDTO> followers;
    private  List<FollowDTO> following;

}
