package com.example.HealthAndFitnessPlatform.controller;

import com.example.HealthAndFitnessPlatform.dto.LikeDTO;
import com.example.HealthAndFitnessPlatform.model.Like;
import com.example.HealthAndFitnessPlatform.service.LikeService;
import org.springframework.boot.autoconfigure.batch.BatchTransactionManager;
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

        @GetMapping("/user/{userId}")
        public ResponseEntity<LikeDTO> findLikeByUserId(@PathVariable int userId){
                 LikeDTO like = likeService.findLikeByUserId(userId);
                 return new ResponseEntity<>(like,HttpStatus.OK);
        }

        @GetMapping("/getLikesById/{userId}")
        public ResponseEntity<List<LikeDTO>> findLikesByUserId(@PathVariable int userId){
                        List<LikeDTO> likes = likeService.findLikesByUserId(userId);
                        return new ResponseEntity<>(likes,HttpStatus.OK);
        }


}
