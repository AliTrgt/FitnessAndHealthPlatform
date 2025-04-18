package com.example.HealthAndFitnessPlatform.controller;


import com.example.HealthAndFitnessPlatform.model.WorkoutPlan;
import com.example.HealthAndFitnessPlatform.model.WorkoutType;
import com.example.HealthAndFitnessPlatform.repository.WorkoutPlanRepository;
import lombok.Getter;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/workout")
public class WorkoutController {


    private final WorkoutPlanRepository workoutPlanRepository;

    public WorkoutController(WorkoutPlanRepository workoutPlanRepository) {
        this.workoutPlanRepository = workoutPlanRepository;
    }

    @GetMapping("/types")
    public WorkoutType[] getWorkoutTypes(){
            return WorkoutType.values();
    }

    @PostMapping("/create")
    public WorkoutPlan createWorkOutPlan(@RequestBody WorkoutPlan workoutPlan){
        return workoutPlanRepository.save(workoutPlan);
    }


}
