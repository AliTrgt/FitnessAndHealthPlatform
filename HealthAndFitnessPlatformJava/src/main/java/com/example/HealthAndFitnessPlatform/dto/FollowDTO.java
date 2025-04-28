package com.example.HealthAndFitnessPlatform.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FollowDTO {

    private int id;
    private int followerId;
    private int followingId;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-mm-dd HH:mm:ss")
    private  LocalDateTime createdAt;
}
