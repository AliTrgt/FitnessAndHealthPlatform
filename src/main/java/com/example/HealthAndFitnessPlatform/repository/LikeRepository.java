package com.example.HealthAndFitnessPlatform.repository;

import com.example.HealthAndFitnessPlatform.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like,Integer> {

    Optional<Like> findByUserIdAndRecipeId(int userId, int recipeId);

    boolean existsLikeByUserIdAndRecipeId(int userId,int recipeId);

    Like findLikeByUserId(int userId);

    List<Like> findLikesByUserId(int userId);
}
