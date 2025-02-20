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

        @GetMapping
        public ResponseEntity<List<LikeDTO>> getAllLike(){
                List<LikeDTO> likeList = likeService.getAllLike();
                return new ResponseEntity<>(likeList, HttpStatus.OK);
        }

        @PostMapping("/like")
        public ResponseEntity<LikeDTO> like(@RequestParam int userId,@RequestParam int recipeId){
                LikeDTO likeDTO  = likeService.createLike(userId,recipeId);
                return new ResponseEntity<>(likeDTO,HttpStatus.CREATED);
        }

        @DeleteMapping("/dislike")
        public ResponseEntity<Void> dislike(@RequestParam int userId,@RequestParam int recipeId){
                likeService.deleteLike(userId,recipeId);
                return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        @PostMapping("/toggleLike")
        public ResponseEntity<LikeDTO> toggleLike(@RequestParam int userId,@RequestParam int recipeId){
                LikeDTO likeDTO = likeService.toggleLike(userId,recipeId);
                return new ResponseEntity<>(likeDTO,HttpStatus.CREATED);
        }


}
