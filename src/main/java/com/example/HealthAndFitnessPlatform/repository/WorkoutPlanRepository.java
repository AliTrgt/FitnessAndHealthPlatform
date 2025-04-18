package com.example.HealthAndFitnessPlatform.repository;

import com.example.HealthAndFitnessPlatform.model.WorkoutPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkoutPlanRepository extends JpaRepository<WorkoutPlan,Integer> {
}
