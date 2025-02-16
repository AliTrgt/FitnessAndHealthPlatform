package com.example.HealthAndFitnessPlatform.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GeneralExceptionHandler {

        @ExceptionHandler(UserNotFoundException.class)
        public ResponseEntity<?> handleUser(){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        @ExceptionHandler(CommentNotFoundException.class)
        public ResponseEntity<?> handleComment(){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }



}
