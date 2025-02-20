package com.example.HealthAndFitnessPlatform.service;

import com.example.HealthAndFitnessPlatform.dto.FollowDTO;
import com.example.HealthAndFitnessPlatform.repository.FollowRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class FollowService {

        private final FollowRepository followRepository;
        private final ModelMapper modelMapper;

        public FollowService(FollowRepository followRepository, ModelMapper modelMapper) {
                this.followRepository = followRepository;
                this.modelMapper = modelMapper;
        }


        public FollowDTO followUser(int followingId,int followerId){
                        return null;
        }


}
