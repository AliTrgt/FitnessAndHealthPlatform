package com.example.HealthAndFitnessPlatform.service;

import com.example.HealthAndFitnessPlatform.dto.AuthRequest;
import com.example.HealthAndFitnessPlatform.dto.UserDTO;
import com.example.HealthAndFitnessPlatform.exception.UserNotFoundException;
import com.example.HealthAndFitnessPlatform.model.User;
import com.example.HealthAndFitnessPlatform.repository.UserRepository;
import com.example.HealthAndFitnessPlatform.security.JwtService;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
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
        double height = user.getHeight();
        double weight = user.getWeight();
        double BMI = (weight / (height * height)) * 10000;
        user.setBMI(BMI);
        User tempUser = userRepository.save(user);
        return modelMapper.map(tempUser,UserDTO.class);
    }

    public UserDTO updateUser(int userId,UserDTO userDTO){
            User tempUser = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User id can not found : "+userId));

            tempUser.setUsername(userDTO.getUsername());
            tempUser.setEmail(userDTO.getEmail());
            tempUser.setProfilePhoto(userDTO.getProfilePhoto());
            tempUser.setHeight(userDTO.getHeight());
            tempUser.setWeight(userDTO.getWeight());
            tempUser.setGender(userDTO.getGender());
            tempUser.setAge(userDTO.getAge());

            User lastUser = userRepository.save(tempUser);
            return modelMapper.map(lastUser,UserDTO.class);

    }

    public void deleteUser(int userId){
           User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User id can not found : "+userId));
           userRepository.delete(user);
    }


    public Map<String,String> login(AuthRequest authRequest){
        Authentication authentication  = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.username(),authRequest.password()));
        if (authentication.isAuthenticated()){
                String accessToken = jwtService.generateToken(authRequest.username());
                Map<String,String> tokens = new HashMap<>();
                tokens.put("accessToken",accessToken);
                return tokens;
        }
        throw new UsernameNotFoundException("Invalid Username : "+authRequest.username());
    }

    public String register(AuthRequest authRequest){
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.username(),authRequest.password()));
            if (authentication.isAuthenticated()){
                    return jwtService.generateToken(authRequest.username());
            }
            throw new UsernameNotFoundException("Invalid Username : "+authRequest.username());
    }

}
