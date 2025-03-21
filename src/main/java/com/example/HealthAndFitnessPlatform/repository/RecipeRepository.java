package com.example.HealthAndFitnessPlatform.repository;

import com.example.HealthAndFitnessPlatform.dto.RecipeDTO;
import com.example.HealthAndFitnessPlatform.model.Recipe;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe,Integer> {

    @Modifying
    @Transactional
    @Query("UPDATE Recipe r SET r.likeCount = r.likeCount + 1 WHERE r.id = :recipeId")
    int incrementLikeCount(@Param("recipeId") int recipeId);

    @Modifying
    @Transactional
    @Query("UPDATE Recipe r SET r.likeCount = r.likeCount - 1 WHERE r.id = :recipeId AND r.likeCount > 0")
    int decrementLikeCount(@Param("recipeId") int recipeId);

    List<Recipe> getRecipesByUserId(int userId);
}
