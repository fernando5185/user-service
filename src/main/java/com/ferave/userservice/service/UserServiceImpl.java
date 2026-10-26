package com.ferave.userservice.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ferave.userservice.dto.UserRequestDto;
import com.ferave.userservice.dto.UserResponseDto;
import com.ferave.userservice.dto.UserUpdateDto;
import com.ferave.userservice.entity.User;
import com.ferave.userservice.exceptions.EmailAlreadyExistsException;
import com.ferave.userservice.exceptions.ResourceNotFoundException;
import com.ferave.userservice.exceptions.UserNameAlreadyExistsException;
import com.ferave.userservice.mapper.UserMapper;
import com.ferave.userservice.repository.UserRepository;

@Service
@org.springframework.transaction.annotation.Transactional (readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    private void validateEmail(UserUpdateDto userUpdateDto, User existingUser) {
        if (userUpdateDto.email() == null || userUpdateDto.email().equals(existingUser.getEmail())){
            return;
        }

        if (userRepository.existsByEmailAndIdNot(userUpdateDto.email(), existingUser.getId())) {
            throw new EmailAlreadyExistsException("Email " + userUpdateDto.email() + " already exists");
        }
    }

    private void validateUserName(UserUpdateDto userUpdateDto, User existingUser) {
        if (userUpdateDto.userName() == null || userUpdateDto.userName().equals(existingUser.getUserName())) {
            return;
        }

        if (userRepository.existsByUserNameAndIdNot(userUpdateDto.userName(), existingUser.getId())) {
            throw new UserNameAlreadyExistsException("Username already exists");
        }
    }

    @Override
    @Transactional 
    public UserResponseDto saveUser(UserRequestDto userRequestDto) {

        if (userRepository.existsByEmail(userRequestDto.email())){
            throw new EmailAlreadyExistsException(
                "Email already exists"
            );
        }

        if (userRepository.existsByUserName(userRequestDto.userName())) {
            throw new UserNameAlreadyExistsException(
                "Username already exists"
            );
        }
        
        User entity = userMapper.toEntity(userRequestDto);
        
        User saveUser =userRepository.save(entity);
        
        return userMapper.toDto(saveUser);
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
    
}
