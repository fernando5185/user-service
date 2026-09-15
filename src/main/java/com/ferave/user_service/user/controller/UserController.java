package com.ferave.user_service.user.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ferave.user_service.user.dto.UserRequestDto;
import com.ferave.user_service.user.dto.UserResponseDto;
import com.ferave.user_service.user.service.UserService;

import lombok.AllArgsConstructor;

@RestController 
@RequestMapping (path = "api/v1/user")
@AllArgsConstructor 
public class UserController {
    
    private final UserService userService;

    @PostMapping 
    public ResponseEntity<UserResponseDto> saveUser(@RequestBody UserRequestDto userRequestDto) {
        UserResponseDto userResponseDto = userService.savedUser(userRequestDto);
        return ResponseEntity.ok().body(userResponseDto);
    }

    @GetMapping("/entities")
    public ResponseEntity<List<UserResponseDto>> findAllUsers(){
        List<UserResponseDto> findAll = userService.users();
        return ResponseEntity.ok().body(findAll);
    }
}
