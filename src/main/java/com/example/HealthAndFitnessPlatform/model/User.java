package com.example.HealthAndFitnessPlatform.model;


import io.micrometer.common.lang.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "userTBL")
@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull(message = "Username can not be Null !!")
    private String username;

    @NotNull
    @Size(min = 8,max = 16,message = "Password must be least 8 character")
    private String password;

    @Email(message = "Invalid email try again !! ")
    @NotNull
    private String email;

    private String profilePhoto;

    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Recipe> recipeList;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Like> likeList;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Comment> commentList;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Favorite> favoriteList;

    @OneToMany(mappedBy = "fallowersId", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Fallow> fallowers;

    @OneToMany(mappedBy = "fallowingId",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Fallow> fallowing;

}
