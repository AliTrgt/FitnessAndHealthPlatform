package com.example.HealthAndFitnessPlatform.model;

public enum WorkoutType {

    RUNNING(10),
    WEIGHTLIFTING(6),
    STRENGTH_TRAINING(4);

    private final int caloriesBurnedPerMinute;

    WorkoutType(int caloriesBurnedPerMinute) {
        this.caloriesBurnedPerMinute = caloriesBurnedPerMinute;
    }

    public int getCaloriesBurnedPerMinute() {
        return caloriesBurnedPerMinute;
    }
}
