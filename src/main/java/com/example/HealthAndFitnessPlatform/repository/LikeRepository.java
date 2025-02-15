package com.example.HealthAndFitnessPlatform.repository;

import com.example.HealthAndFitnessPlatform.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like,Integer> {
}
