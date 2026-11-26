package com.ferave.userservice.user.service;

import java.util.Objects;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ferave.userservice.identity.port.UserIdentityProvider;
import com.ferave.userservice.shared.exceptions.domain.EmailAlreadyExistsException;
import com.ferave.userservice.shared.exceptions.domain.ResourceNotFoundException;
import com.ferave.userservice.shared.exceptions.domain.UserCreateException;
import com.ferave.userservice.shared.exceptions.domain.UserNameAlreadyExistsException;
import com.ferave.userservice.shared.exceptions.integration.KeycloakIntegrationException;
import com.ferave.userservice.user.dto.UserRequestDto;
import com.ferave.userservice.user.dto.UserResponseDto;
import com.ferave.userservice.user.dto.UserUpdateDto;
import com.ferave.userservice.user.entity.User;
import com.ferave.userservice.user.mapper.UserMapper;
import com.ferave.userservice.user.repository.UserRepository;

@Service
@org.springframework.transaction.annotation.Transactional (readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserIdentityProvider userIdentityProvider;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper,
            UserIdentityProvider userIdentityProvider) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userIdentityProvider = userIdentityProvider;
    }

    // Register User in Database and Keycloak
    @Override
    public UserResponseDto saveUser(UserRequestDto userRequestDto) {

        if (userRepository.existsByEmail(userRequestDto.email())){
            throw new EmailAlreadyExistsException(
                "Email already exists"
            );
        }

        if (userRepository.existsByUsername(userRequestDto.username())) {
            throw new UserNameAlreadyExistsException(
                "Username already exists"
            );
        }

        User entity = userMapper.toEntity(userRequestDto);

        UserResponseDto response;

        try {
            response = userIdentityProvider.saveUserIdentity(userRequestDto);
        } catch (Exception e) {
            throw new KeycloakIntegrationException("Failed to create user identity in Keycloak", e); 
        }

        String keycloakId = response.keycloakId();

        entity.setKeycloakId(keycloakId);

        try {
            entity = userRepository.save(entity);    
        } catch (Exception e) {
            try {
                userIdentityProvider.deleteUserIdentityById(keycloakId);
            } catch (Exception comppensationException) {
                throw new KeycloakIntegrationException(comppensationException);
            }
            throw new UserCreateException("Failed to create user in the database", e);
        }
        
        return userMapper.toDto(entity);
        
    }

    // Register User in Database
    @Override
    @Transactional 
    public UserResponseDto registerUser(UserRequestDto userRequestDto) {
        
        if (userRepository.existsByEmail(userRequestDto.email())){
            throw new EmailAlreadyExistsException(
                "Email already exists"
            );
        }

        if (userRepository.existsByUsername(userRequestDto.username())) {
            throw new UserNameAlreadyExistsException(
                "Username already exists"
            );
        }

        User entity = userMapper.toEntity(userRequestDto);

        User savedUser = userRepository.save(entity);

        return userMapper.toDto(savedUser);
    }

    @Override 
    public Page<UserResponseDto> users(Pageable pageable) {
        Page<User> findUsers = userRepository.findAll(pageable);
        return userMapper
            .toDtoList(findUsers);           
    }

    @Override
    public UserResponseDto findUserById(UUID id) {
       return userRepository.findById(id).map(userMapper::toDto)
            .orElseThrow(() -> 
                new ResourceNotFoundException("User with id "+ id + " not found")
            );
    }

    @Override
    @Transactional 
    public UserResponseDto updateUser(UserUpdateDto userUpdateDto, UUID id) {
        User existingUser = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));

        validateEmail(userUpdateDto, existingUser);

        validateUserName(userUpdateDto, existingUser);

        userMapper.updateUser(userUpdateDto, existingUser);

        User updateUser = userRepository.save(existingUser);

        return userMapper.toDto(updateUser);
    }

    @Override
    @Transactional 
    public void deleteUserById(UUID id) {
        User exitsingUser = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
        userRepository
            .delete(exitsingUser);
    }

    private void validateEmail(UserUpdateDto userUpdateDto, User existingUser) {
        if (userUpdateDto.email() == null || 
                userUpdateDto.email().equals(existingUser.getEmail())){
            return;
        }

        if (userRepository.existsByEmailAndIdNot(userUpdateDto.email(), existingUser.getId())) {
            throw new EmailAlreadyExistsException(
                "Email " + userUpdateDto.email() + " already exists"
            );
        }
    }

    private void validateUserName(UserUpdateDto userUpdateDto, User existingUser) {
        if (userUpdateDto.username() == null || 
            Objects.equals(userUpdateDto.username(), existingUser.getUsername())) {
        return;
        }

        if (userRepository.existsByUsernameAndIdNot(userUpdateDto.username(), existingUser.getId())) {
            throw new UserNameAlreadyExistsException(
                "Username " + userUpdateDto.username() + " already exists"
            );
        }
    }
    
}
