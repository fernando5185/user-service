package com.ferave.userservice.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import com.ferave.userservice.integration.port.UserIdentityProvider;
import com.ferave.userservice.service.UserService;

import jakarta.validation.Valid;

@RestController 
@RequestMapping (path = "/api/v1/users")
public class UserController {
    
    private final UserService userService;
    private final UserIdentityProvider userIdentityProvider;

    public UserController(UserService userService, UserIdentityProvider userIdentityProvider) {
        this.userService = userService;
        this.userIdentityProvider = userIdentityProvider;
    }

    @PostMapping (path = "/register")
    @PreAuthorize ("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> registerUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        UserResponseDto created = userService.registerUser(userRequestDto);

        URI location = ServletUriComponentsBuilder
                            .fromCurrentRequest()
                            .path("/{id}")
                            .buildAndExpand(created.id())
                            .toUri();

        return ResponseEntity
                .created(location)
                .body(created);
    }

    @PostMapping
    @PreAuthorize ("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> saveUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        UserResponseDto created = userService.saveUser(userRequestDto);

        URI location = ServletUriComponentsBuilder
                            .fromCurrentRequest()
                            .path("/{id}")
                            .buildAndExpand(created.id())
                            .toUri();

        return ResponseEntity
                .created(location)
                .body(created);
    }

    @PostMapping (path = "/identity")
    @PreAuthorize ("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> saveUserIdentity(@Valid @RequestBody UserRequestDto userRequestDto) {
        UserResponseDto created = userIdentityProvider.saveUserIdentity(userRequestDto);

        URI location = ServletUriComponentsBuilder
                            .fromCurrentRequest()
                            .path("/{id}")
                            .buildAndExpand(created.id())
                            .toUri();

        return ResponseEntity
                .created(location)
                .body(created);

    }

    @GetMapping (path = "/identities")
    @PreAuthorize ("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDto>> findAllUserIdentities() {
        List<UserResponseDto> findAllIdentities = userIdentityProvider.usersIdentity();
        return ResponseEntity
                .ok()
                .body(findAllIdentities);
    }

    @GetMapping (path = "/identity/{id}")
    @PreAuthorize ("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> findUserIdentityById(@PathVariable(name = "id") String keycloakId) {
        UserResponseDto findIdentity = userIdentityProvider.getUserIdentityById(keycloakId);
        return ResponseEntity
                .ok()
                .body(findIdentity);
    }

    @DeleteMapping (path = "/identity/{id}")
    @PreAuthorize ("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUserIdentityById(@PathVariable(name = "id") String keycloakId) {
        userIdentityProvider.deleteUserIdentityById(keycloakId);
        return ResponseEntity
                .noContent()
                .build();
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
