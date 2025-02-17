package com.example.HealthAndFitnessPlatform.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "commentTBL")
@Data
@AllArgsConstructor
@RequiredArgsConstructor
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

    @Column(name = "createdAt")
    private LocalDateTime createdAt = LocalDateTime.now();

}
