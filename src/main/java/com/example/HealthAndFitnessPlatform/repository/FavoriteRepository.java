package com.example.HealthAndFitnessPlatform.repository;

import com.example.HealthAndFitnessPlatform.model.Favorite;
import com.example.HealthAndFitnessPlatform.model.Recipe;
import com.example.HealthAndFitnessPlatform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite,Integer> {

        Optional<Favorite> findByUserIdAndRecipeId(int userId, int recipeId);

}
