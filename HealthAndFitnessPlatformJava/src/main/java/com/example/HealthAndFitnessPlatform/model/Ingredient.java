package com.example.HealthAndFitnessPlatform.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;

@Entity
@Table(name = "ingredientTBL")
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String name;

    private String quantity;

    @ManyToMany(mappedBy = "ingredientList",fetch = FetchType.LAZY)
    private List<Recipe> recipeList;

    public Ingredient(String name, String quantity) {
        this.name = name;
        this.quantity = quantity;
    }
}
