package com.example.HealthAndFitnessPlatform.controller;


import com.example.HealthAndFitnessPlatform.dto.AuthRequest;
import com.example.HealthAndFitnessPlatform.dto.UserDTO;
import com.example.HealthAndFitnessPlatform.model.User;
import com.example.HealthAndFitnessPlatform.service.UserService;
import jakarta.persistence.GeneratedValue;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/user")
public class UserController {

    private final  UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUser(){
        List<UserDTO> userList = userService.getAllUser();
        return new ResponseEntity<>(userList,HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> findById(@PathVariable int userId){
        UserDTO userDTO =  userService.findById(userId);
        return new ResponseEntity<>(userDTO,HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO user){
        UserDTO userDTO = userService.createUser(user);
        return new ResponseEntity<>(userDTO,HttpStatus.CREATED);
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable int userId,@Valid @RequestBody UserDTO user){
             UserDTO userDTO = userService.updateUser(userId,user);
             return new ResponseEntity<>(userDTO,HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable int userId){
            userService.deleteUser(userId);
             return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/login")
    public Map<String,String> login(@RequestBody AuthRequest authRequest){
            return userService.login(authRequest);
    }

    @PostMapping("/register")
    public String register(@RequestBody AuthRequest authRequest){
            return userService.register(authRequest);
    }
}
