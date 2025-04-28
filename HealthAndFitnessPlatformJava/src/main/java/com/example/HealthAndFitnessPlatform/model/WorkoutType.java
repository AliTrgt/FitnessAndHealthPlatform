package com.example.HealthAndFitnessPlatform.model;

public enum WorkoutType {

    RUNNING(15.1),
    WEIGHTLIFTING(9.0),
    STRENGTH_TRAINING(9.0),
    CYCLING(8.5),
    SWIMMING(8.5),
    YOGA(3.0),
    HIIT(15.5),
    PILATES(5.0),
    WALKING(8.7),
    BOXING(12.0),
    DANCING(7.0),
    CROSSFIT(11.5),
    ROWING(11.25),
    HIKING(7.5),
    JUMP_ROPE(12.0);
    private final double caloriesBurnedPerMinute;

    WorkoutType(double caloriesBurnedPerMinute) {
        this.caloriesBurnedPerMinute = caloriesBurnedPerMinute;
    }

    public double getCaloriesBurnedPerMinute() {
        return caloriesBurnedPerMinute;
    }
}
