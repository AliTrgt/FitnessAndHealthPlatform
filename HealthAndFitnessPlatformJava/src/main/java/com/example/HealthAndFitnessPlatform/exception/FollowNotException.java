package com.example.HealthAndFitnessPlatform.exception;

import com.example.HealthAndFitnessPlatform.model.Follow;

public class FollowNotException extends RuntimeException{

    public FollowNotException(String msg){
            super(msg);
    }
}
