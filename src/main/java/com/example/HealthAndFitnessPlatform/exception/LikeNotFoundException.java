package com.example.HealthAndFitnessPlatform.exception;

import com.example.HealthAndFitnessPlatform.repository.LikeRepository;

public class LikeNotFoundException extends RuntimeException{

    public LikeNotFoundException(String msg){
            super(msg);
    }
}
