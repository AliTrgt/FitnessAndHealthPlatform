package com.example.HealthAndFitnessPlatform.controller;


import com.example.HealthAndFitnessPlatform.dto.CommentDTO;
import com.example.HealthAndFitnessPlatform.model.Comment;
import com.example.HealthAndFitnessPlatform.service.CommentService;
import jakarta.validation.Valid;
import org.hibernate.validator.constraints.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/comment")
public class CommentController {

    private static final Logger log = LoggerFactory.getLogger(CommentController.class);
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public ResponseEntity<List<CommentDTO>> getAllComment(){
            List<CommentDTO> commentList = commentService.getAllComments();
            return new ResponseEntity<>(commentList, HttpStatus.OK);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentDTO> findById(@PathVariable int commentId){
            CommentDTO commentDTO = commentService.findById(commentId);
            return new ResponseEntity<>(commentDTO,HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<CommentDTO> createComment(@Valid @RequestBody CommentDTO comment){
            CommentDTO commentDTO = commentService.createComment(comment);
            return new ResponseEntity<>(commentDTO,HttpStatus.CREATED);
    }

    @PutMapping("/update/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable int commentId,@Valid @RequestBody CommentDTO comment){
            CommentDTO commentDTO = commentService.updateComment(commentId,comment);
            return new ResponseEntity<>(commentDTO,HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable int commentId){
            commentService.deleteComment(commentId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
