package com.example.HealthAndFitnessPlatform.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WorkoutPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    private WorkoutType workoutType;

    private int duration;

    private int burningCalories;

    @PrePersist
    @PreUpdate
    public void calculateBurningCalories() {
        this.burningCalories = duration * workoutType.getCaloriesBurnedPerMinute();
    }

}
