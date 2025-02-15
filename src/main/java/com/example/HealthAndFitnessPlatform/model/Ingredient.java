package com.example.HealthAndFitnessPlatform.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Entity
@Table(name = "ingredientTBL")
@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String name;

    @ManyToMany(mappedBy = "ingredientList",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Recipe> recipeList;

}
