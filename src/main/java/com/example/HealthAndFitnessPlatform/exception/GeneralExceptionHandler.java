package com.example.HealthAndFitnessPlatform.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GeneralExceptionHandler {

        @ExceptionHandler(UserNotFoundException.class)
        public ResponseEntity<?> handleUser(UserNotFoundException exception){
                return new ResponseEntity<>(exception.getMessage(),HttpStatus.NOT_FOUND);
        }

        @ExceptionHandler(CommentNotFoundException.class)
        public ResponseEntity<?> handleComment(CommentNotFoundException exception){
                return new ResponseEntity<>(exception.getMessage(),HttpStatus.NOT_FOUND);
        }

        @ExceptionHandler(RecipeNotFoundException.class)
        public ResponseEntity<?> handleRecipe(RecipeNotFoundException exception){
                return new ResponseEntity<>(exception.getMessage(),HttpStatus.NOT_FOUND);
        }

        @ExceptionHandler(FavoriteNotFoundException.class)
        public ResponseEntity<?> handleFavorite(FavoriteNotFoundException exception){
                        return new ResponseEntity<>(exception.getMessage(),HttpStatus.NOT_FOUND);
        }

        @ExceptionHandler(LikeNotFoundException.class)
        public ResponseEntity<?> handleLike(LikeNotFoundException exception){
                        return new ResponseEntity<>(exception.getMessage(),HttpStatus.NOT_FOUND);
        }



}
