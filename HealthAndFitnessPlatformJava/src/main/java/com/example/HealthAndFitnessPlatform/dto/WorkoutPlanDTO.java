package com.example.HealthAndFitnessPlatform.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WorkoutPlanDTO {
        private int id;
        private String workoutType;
        private int duration;
        private double burningCalories;
        private String workoutMessage;
        private int userId;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createdAt;
}
