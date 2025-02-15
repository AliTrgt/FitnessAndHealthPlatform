package com.example.HealthAndFitnessPlatform.repository;

import com.example.HealthAndFitnessPlatform.model.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<Follow,Integer> {
}
