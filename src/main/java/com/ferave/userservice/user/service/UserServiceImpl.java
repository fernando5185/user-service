package com.ferave.userservice.user.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ferave.userservice.exceptions.AlreadyExistsException;
import com.ferave.userservice.exceptions.NotFoundException;
import com.ferave.userservice.user.dto.UserRequestDto;
import com.ferave.userservice.user.dto.UserResponseDto;
import com.ferave.userservice.user.dto.UserUpdateDto;
import com.ferave.userservice.user.entity.User;
import com.ferave.userservice.user.mapper.UserMapper;
import com.ferave.userservice.user.repository.UserRepository;


@Service 
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserResponseDto savedUser(UserRequestDto userRequestDto) {
        Optional<User> findByEmail = userRepository.findByEmail(userRequestDto.email());
        findByEmail.ifPresent(e -> {
            throw new AlreadyExistsException("Already " + userRequestDto.email() + " exists");
        });
        User entity = userMapper.toEntity(userRequestDto);
        User saveUser =userRepository.save(entity);
        return userMapper.toDto(saveUser);
    }

    @Override
    public List<UserResponseDto> users() {
        List<User> findUsers = userRepository.findAll();
        return userMapper.toDtoList(findUsers);
    }

    @Override
    public Optional<UserResponseDto> findUserById(Long id) {
       return userRepository.findById(id).map(userMapper::toDto);
    }

    @Override
    public UserResponseDto updatedUser(UserUpdateDto userUpdateDto, Long id) {
        User existingUser = userRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("User with id " + id + " not found"));

        userMapper.updateUser(userUpdateDto, existingUser);

        User updateUser = userRepository.save(existingUser);

        return userMapper.toDto(updateUser);
    }

    @Override
    public void deleteUserById(Long id) {
        User exitsingUser = userRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("User with id " + id + " not found"));
        userRepository.delete(exitsingUser);
    }
    
}
