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

    @PostMapping
    public ResponseEntity<FollowDTO> follow(@RequestBody FollowDTO follow){
            FollowDTO followDTO = followService.follow(follow.getFollowerId(),follow.getFollowingId());
            return new ResponseEntity<>(followDTO,HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<Void> unFollow(@RequestBody FollowDTO follow){
             followService.unFollow(follow.getFollowerId(),follow.getFollowingId());
             return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/isFollow")
    public boolean isFollow(@RequestParam int followerId,@RequestParam int followingId){
            return followService.isFollowing(followerId,followingId);
    }

}
