package com.example.HealthAndFitnessPlatform.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "ingredientTBL")
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String name;

    @ManyToMany(mappedBy = "ingredientList",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Recipe> recipeList;

}
