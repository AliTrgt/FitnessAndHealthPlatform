package com.example.HealthAndFitnessPlatform.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Formula;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "userTBL")
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull(message = "Username can not be Null !!")
    private String username;

    @NotNull
    @Size(min = 8,max = 16,message = "Password must be least 8 character")
    private String password;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "roleId"))
    private List<Role> authorities;

    @Email(message = "Invalid email try again !! ")
    @NotNull
    private String email;

    private String profilePhoto;

    @NotNull(message = "Height should be CM !!")
    private double height;

    @NotNull
    @Size(min = 30,max = 200)
    private double weight;

    @Formula("weight / (height * height)")
    private double BMI;

    @Column(name = "createdAt")
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Recipe> recipeList = new ArrayList<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Like> likeList = new ArrayList<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Favorite> favoriteList = new ArrayList<>();

    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Follow> followers = new ArrayList<>();

    @OneToMany(mappedBy = "following",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Follow> following = new ArrayList<>();

}
