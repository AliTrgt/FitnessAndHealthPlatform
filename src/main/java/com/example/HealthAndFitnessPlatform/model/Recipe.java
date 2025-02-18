package com.example.HealthAndFitnessPlatform.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Lazy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "recipeTBL")
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull(message = "You must add title section")
    private String title;

    @NotNull(message = "You must add description section")
    @Lob
    private String description;

    @NotNull(message = "You must add instructions section")
    @Lob
    private String instructions;

    @NotNull
    @Max(5)
    private int prepTime;

    @NotNull
    @Max(1500)
    private double calories;

    private LocalDateTime createdAt = LocalDateTime.now();

    @NotNull
    private int likeCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;


    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinTable(
            name = "recipeIngredients",
            joinColumns = @JoinColumn(name = "recipeId"),
            inverseJoinColumns = @JoinColumn(name = "ingredientId")
    )
    private List<Ingredient> ingredientList;

    @OneToMany(mappedBy = "recipe",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Like> likeList = new ArrayList<>();

    @OneToMany(mappedBy = "recipe",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Comment> commentList = new ArrayList<>();

}
