package com.example.HealthAndFitnessPlatform.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "commentTBL")
@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "recipeId")
    private Recipe recipe;

    @Lob
    @NotNull(message = "You must write a comment !!")
    private String content;

    private LocalDateTime createdAt = LocalDateTime.now();

}
