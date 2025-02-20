package com.example.HealthAndFitnessPlatform.controller;

import com.example.HealthAndFitnessPlatform.dto.FollowDTO;
import com.example.HealthAndFitnessPlatform.model.Follow;
import com.example.HealthAndFitnessPlatform.service.FollowService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/follow")
public class FollowController {

    private final FollowService followService;

    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    @GetMapping
    public ResponseEntity<List<FollowDTO>> getAllFollow(){
            List<FollowDTO> followList = followService.getAllFollow();
            return new ResponseEntity<>(followList, HttpStatus.OK);
    }

    @PostMapping("/follow")
    public ResponseEntity<FollowDTO> follow(@RequestParam int followerId,@RequestParam int followingId){
            FollowDTO followDTO = followService.follow(followerId,followingId);
            return new ResponseEntity<>(followDTO,HttpStatus.CREATED);
    }

    @PostMapping("/unfollow")
    public ResponseEntity<Void> unFollow(@RequestParam int followerId,@RequestParam int followingId){
             followService.unFollow(followerId,followingId);
             return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
