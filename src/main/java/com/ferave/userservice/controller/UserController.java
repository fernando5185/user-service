package com.ferave.userservice.controller;

import java.net.URI;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatusCode;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ferave.userservice.dto.UserRequestDto;
import com.ferave.userservice.dto.UserResponseDto;
import com.ferave.userservice.dto.UserUpdateDto;
import com.ferave.userservice.service.UserService;

import jakarta.validation.Valid;

@RestController 
@RequestMapping (path = "/api/v1/users")
public class UserController {
    
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @PreAuthorize ("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> saveUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        UserResponseDto created = userService.saveUser(userRequestDto);

        URI location = ServletUriComponentsBuilder
                            .fromCurrentRequest()
                            .path("{/id}")
                            .buildAndExpand(created.id())
                            .toUri();

        return ResponseEntity
                .status(HttpStatusCode.valueOf(201))
                .location(location)
                .body(created);
    }

    @GetMapping
    @PreAuthorize ("hasRole('ADMIN')")
    public ResponseEntity<Page<UserResponseDto>> findAllUsers(Pageable pageable){
        Page<UserResponseDto> findAll = userService.users(pageable);
        return ResponseEntity
                .ok()
                .body(findAll);
    }

    @GetMapping("/{id}")
    @PreAuthorize ("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> findUserById(@PathVariable(name = "id") UUID id) {
        return ResponseEntity.
            ok(userService
                .findUserById(id));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> updateUser(@Valid @RequestBody  UserUpdateDto userUpdateDto, @PathVariable(name = "id") UUID id) {
        UserResponseDto update = userService.updateUser(userUpdateDto, id);
        return ResponseEntity
                .ok()
                .body(update);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable(name = "id") UUID id) {
        userService.deleteUserById(id);
        return ResponseEntity
                .noContent()
                .build();
        }
}
