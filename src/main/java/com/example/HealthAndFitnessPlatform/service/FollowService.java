package com.example.HealthAndFitnessPlatform.service;

import com.example.HealthAndFitnessPlatform.dto.FollowDTO;
import com.example.HealthAndFitnessPlatform.exception.FollowNotException;
import com.example.HealthAndFitnessPlatform.exception.UserNotFoundException;
import com.example.HealthAndFitnessPlatform.model.Follow;
import com.example.HealthAndFitnessPlatform.model.User;
import com.example.HealthAndFitnessPlatform.repository.FollowRepository;
import com.example.HealthAndFitnessPlatform.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FollowService {

        private final FollowRepository followRepository;
        private final ModelMapper modelMapper;
        private final UserRepository userRepository;

        public FollowService(FollowRepository followRepository, ModelMapper modelMapper, UserRepository userRepository) {
                this.followRepository = followRepository;
                this.modelMapper = modelMapper;
                this.userRepository = userRepository;
        }

        public List<FollowDTO> getAllFollow(){
                List<Follow> followList = followRepository.findAll();
                return followList.isEmpty() ? Collections.emptyList() : followList
                        .stream()
                        .map(follow ->modelMapper.map(follow,FollowDTO.class))
                        .collect(Collectors.toList());
        }


        public FollowDTO follow(int followerId,int followingId){
                User follower = userRepository.findById(followerId).orElseThrow(() -> new UserNotFoundException("followerId not found : "+followerId));
                User following = userRepository.findById(followingId).orElseThrow(() -> new UserNotFoundException("following not found : "+followingId));

                Follow follow = new Follow();
                follow.setFollower(follower);
                follow.setFollowing(following);
                follow.setCreatedAt(LocalDateTime.now());
                Follow lastFollow = followRepository.save(follow);

                return   modelMapper.map(lastFollow,FollowDTO.class);
        }

        public void unFollow(int followerId,int followingId){
                       Follow follow = followRepository.findByFollowerIdAndFollowingId(followerId,followingId).orElseThrow(() -> new FollowNotException(followerId+" Is not following : "+followingId));

                       followRepository.delete(follow);
        }


}
