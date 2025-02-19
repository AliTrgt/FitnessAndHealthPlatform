package com.example.HealthAndFitnessPlatform.service;

import com.example.HealthAndFitnessPlatform.dto.DTOConverter;
import com.example.HealthAndFitnessPlatform.dto.FollowDTO;
import com.example.HealthAndFitnessPlatform.repository.FollowRepository;
import org.springframework.stereotype.Service;

@Service
public class FollowService {

        private final FollowRepository followRepository;
        private final DTOConverter dtoConverter;

        public FollowService(FollowRepository followRepository, DTOConverter dtoConverter) {
                this.followRepository = followRepository;
                this.dtoConverter = dtoConverter;
        }


        public FollowDTO followUser(int followingId,int followerId){

        }


}
