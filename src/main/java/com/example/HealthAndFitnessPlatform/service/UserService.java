package com.example.HealthAndFitnessPlatform.service;

import com.example.HealthAndFitnessPlatform.dto.DTOConverter;
import com.example.HealthAndFitnessPlatform.dto.UserDTO;
import com.example.HealthAndFitnessPlatform.exception.UserNotFoundException;
import com.example.HealthAndFitnessPlatform.model.User;
import com.example.HealthAndFitnessPlatform.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final DTOConverter dtoConverter;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, DTOConverter dtoConverter, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.dtoConverter = dtoConverter;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserDTO> getAllUser(){
            return userRepository.findAll()
                    .stream()
                    .map(dtoConverter::convertToUserDTO)
                    .collect(Collectors.toList());
    }

    public UserDTO findById(int userId){
            User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User id can not found : "+userId));
            return dtoConverter.convertToUserDTO(user);
    }

    public UserDTO createUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User tempUser = userRepository.save(user);
        return dtoConverter.convertToUserDTO(tempUser);
    }

    public UserDTO updateUser(int userId,User user){
            User tempUser = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User id can not found : "+userId));
            tempUser.setUsername(user.getUsername());

            if(!user.getPassword().equals(tempUser.getPassword())){
                tempUser.setPassword(passwordEncoder.encode(user.getPassword()));
            }

            tempUser.setEmail(user.getEmail());
            tempUser.setProfilePhoto(user.getProfilePhoto());

            User lastUser = userRepository.save(tempUser);

            return dtoConverter.convertToUserDTO(lastUser);

    }

    public void deleteUser(int userId){
           User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User id can not found : "+userId));
           userRepository.delete(user);
    }


}
