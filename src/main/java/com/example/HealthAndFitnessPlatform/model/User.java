package com.example.HealthAndFitnessPlatform.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.micrometer.common.lang.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.Formula;
import org.springframework.beans.Mergeable;
import org.springframework.security.core.userdetails.UserDetails;

import java.beans.Transient;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "userTBL")
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull(message = "Username can not be Null !!")
    private String username;

    @NotNull(message = "Password must be least 8 character")
    private String password;

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.MERGE)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "roleId"))
    private List<Role> authorities = new ArrayList<>();

    @Email(message = "Invalid email try again !! ")
    @NotNull
    private String email;

    private String profilePhoto;

    @NotNull(message = "Height should be CM !!")
    private double height;

    @NotNull
    @Max(200)
    private double weight;

    private String gender;

    private int age;

    private double BMI;

    @Column(name = "createdAt")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Recipe> recipeList = new ArrayList<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE,fetch = FetchType.LAZY,orphanRemoval = true)
    private List<Like> likeList = new ArrayList<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE,fetch = FetchType.LAZY)
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE,fetch = FetchType.EAGER)
    private List<Favorite> favoriteList = new ArrayList<>();

    @OneToMany(mappedBy = "follower", cascade = CascadeType.REMOVE,fetch = FetchType.EAGER)
    private List<Follow> followers = new ArrayList<>();

    @OneToMany(mappedBy = "following",cascade = CascadeType.REMOVE,fetch = FetchType.EAGER)
    private List<Follow> following = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", height=" + height +
                ", weight=" + weight +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                '}';
    }
}
