package com.example.HealthAndFitnessPlatform.repository;

import com.example.HealthAndFitnessPlatform.model.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow,Integer> {

   Optional<Follow> findByFollowerIdAndFollowingId(int followerId,int followingId);

   boolean existsByFollowerIdAndFollowingId(int followerId, int followingId);

}
