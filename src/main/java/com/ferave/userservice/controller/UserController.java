package com.ferave.userservice.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ferave.userservice.dto.UserRequestDto;
import com.ferave.userservice.dto.UserResponseDto;
import com.ferave.userservice.dto.UserUpdateDto;
import com.ferave.userservice.service.UserService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController 
@RequestMapping (path = "/api/v1/users/")
@Slf4j
public class UserController {
    
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @PreAuthorize ("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> saveUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        UserResponseDto userResponseDto = userService.saveUser(userRequestDto);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(userResponseDto);
    }

    @GetMapping
    @PreAuthorize ("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDto>> findAllUsers(){
        List<UserResponseDto> findAll = userService.users();
        return ResponseEntity.ok().body(findAll);
    }

    @GetMapping("/{id}")
    @PreAuthorize ("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<UserResponseDto> findUserById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.
            ok(userService
                .findUserById(id));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> updateUser(@Valid @RequestBody  UserUpdateDto userUpdateDto, @PathVariable(name = "id") Long id) {
        UserResponseDto update = userService.updateUser(userUpdateDto, id);
        return ResponseEntity
            .ok()
            .body(update);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable(name = "id") Long id) {
        userService.deleteUserById(id);
        return ResponseEntity
            .noContent()
            .build();
    }
}
