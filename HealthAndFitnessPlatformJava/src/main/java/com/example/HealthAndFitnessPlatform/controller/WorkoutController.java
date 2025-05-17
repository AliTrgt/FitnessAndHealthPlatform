package com.example.HealthAndFitnessPlatform.controller;


import com.example.HealthAndFitnessPlatform.dto.WorkoutPlanDTO;
import com.example.HealthAndFitnessPlatform.model.WorkoutPlan;
import com.example.HealthAndFitnessPlatform.model.WorkoutType;
import com.example.HealthAndFitnessPlatform.repository.WorkoutPlanRepository;
import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/workout")
public class WorkoutController {


    private final WorkoutPlanRepository workoutPlanRepository;
    private final ModelMapper modelMapper;

    public WorkoutController(WorkoutPlanRepository workoutPlanRepository, ModelMapper modelMapper) {
        this.workoutPlanRepository = workoutPlanRepository;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/types")
    public WorkoutType[] getWorkoutTypes(){
            return WorkoutType.values();
    }

    @PostMapping("/create")
    public WorkoutPlanDTO createWorkOutPlan(@RequestBody WorkoutPlanDTO workoutPlanDTO){
            WorkoutPlan workoutPlans = modelMapper.map(workoutPlanDTO, WorkoutPlan.class);

            if (workoutPlans.getCreatedAt() == null) {   // modelmapper override ediyor mecbur buradan implemente edeceğiz.
            workoutPlans.setCreatedAt(LocalDateTime.now());

            }

            workoutPlanRepository.save(workoutPlans);
            return modelMapper.map(workoutPlans, WorkoutPlanDTO.class);
    }


    @DeleteMapping("/{id}")
    public void deleteWorkoutPlan(@PathVariable int id){
              workoutPlanRepository.deleteById(id);
    }

    @GetMapping("/{id}")
    public List<WorkoutPlanDTO> getWorkoutPlanByUserId(@PathVariable int id){
            List<WorkoutPlan> workoutPlans =  workoutPlanRepository.getWorkoutPlanByUserId(id);
            return workoutPlans
                    .stream()
                    .map(wplan -> modelMapper.map(wplan, WorkoutPlanDTO.class))
                    .collect(Collectors.toList());
    }


}
