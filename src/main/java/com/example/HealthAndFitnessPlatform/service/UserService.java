package com.example.HealthAndFitnessPlatform.service;

import com.example.HealthAndFitnessPlatform.dto.UserDTO;
import com.example.HealthAndFitnessPlatform.exception.UserNotFoundException;
import com.example.HealthAndFitnessPlatform.model.User;
import com.example.HealthAndFitnessPlatform.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public UserService(UserRepository userRepository,PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    public List<UserDTO> getAllUser(){
        List<User> userList = userRepository.findAll();
        return userList.isEmpty() ? Collections.emptyList() : userList
                    .stream()
                    .map(user -> modelMapper.map(user,UserDTO.class))
                    .collect(Collectors.toList());
    }

    public UserDTO findById(int userId){
            User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User id can not found : "+userId));
            return modelMapper.map(user,UserDTO.class);
    }

    public UserDTO createUser(UserDTO userDTO){
        User user = modelMapper.map(userDTO,User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User tempUser = userRepository.save(user);
        return modelMapper.map(tempUser,UserDTO.class);
    }

    public UserDTO updateUser(int userId,UserDTO userDTO){
            User tempUser = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User id can not found : "+userId));

            tempUser.setUsername(userDTO.username());
            tempUser.setEmail(userDTO.email());
            tempUser.setProfilePhoto(userDTO.profilePhoto());
            tempUser.setHeight(userDTO.height());
            tempUser.setWeight(userDTO.weight());

            User lastUser = userRepository.save(tempUser);
            return modelMapper.map(lastUser,UserDTO.class);

    }

    public void deleteUser(int userId){
           User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User id can not found : "+userId));
           userRepository.delete(user);
    }


}
