package com.example.HealthAndFitnessPlatform.exception;

import com.example.HealthAndFitnessPlatform.repository.FavoriteRepository;

public class FavoriteNotFoundException extends RuntimeException{

    public FavoriteNotFoundException(String msg){
            super(msg);
    }
}
