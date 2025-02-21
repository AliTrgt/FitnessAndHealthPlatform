package com.example.HealthAndFitnessPlatform.controller;

import com.example.HealthAndFitnessPlatform.dto.LikeDTO;
import com.example.HealthAndFitnessPlatform.service.LikeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/like")
public class LikeController {

        private final LikeService likeService;

        public LikeController(LikeService likeService) {
            this.likeService = likeService;
        }

        @GetMapping("/getAll")
        public ResponseEntity<List<LikeDTO>> getAllLike(){
                List<LikeDTO> likeList = likeService.getAllLike();
                return new ResponseEntity<>(likeList, HttpStatus.OK);
        }

        @PostMapping
        public ResponseEntity<LikeDTO> like(@RequestBody LikeDTO likeDTO){
                LikeDTO like  = likeService.createLike(likeDTO.getUserId(), likeDTO.getRecipeId());
                return new ResponseEntity<>(like,HttpStatus.CREATED);
        }

        @DeleteMapping("/dislike")
        public ResponseEntity<Void> dislike(@RequestBody LikeDTO likeDTO){
                likeService.deleteLike(likeDTO.getUserId(),likeDTO.getRecipeId());
                return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        @PostMapping("/toggleLike")
        public ResponseEntity<LikeDTO> toggleLike(@RequestBody LikeDTO likeDTO){
                LikeDTO like = likeService.toggleLike(likeDTO.getUserId(),likeDTO.getRecipeId());
                return new ResponseEntity<>(like,HttpStatus.CREATED);
        }


}
