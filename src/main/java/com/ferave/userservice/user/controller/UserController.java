package com.ferave.userservice.user.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ferave.userservice.user.dto.UserRequestDto;
import com.ferave.userservice.user.dto.UserResponseDto;
import com.ferave.userservice.user.dto.UserUpdateDto;
import com.ferave.userservice.user.service.UserService;

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

    @GetMapping("/{id}")
    public ResponseEntity<Optional<UserResponseDto>> findUserById(@PathVariable(name = "id") Long id) {
        Optional<UserResponseDto> findById = userService.findUserById(id);
        return ResponseEntity.ok().body(findById);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@RequestBody  UserUpdateDto userUpdateDto, @PathVariable(name = "id") Long id) {
        UserResponseDto update = userService.updatedUser(userUpdateDto, id);
        return ResponseEntity.ok().body(update);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable(name = "id") Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }
}
