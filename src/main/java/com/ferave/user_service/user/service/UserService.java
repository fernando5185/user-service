package com.ferave.user_service.user.service;

import java.util.List;
import java.util.Optional;

import com.ferave.user_service.user.dto.UserRequestDto;
import com.ferave.user_service.user.dto.UserResponseDto;

public interface UserService {

    UserResponseDto saveUser(UserRequestDto userRequestDto);
    List<UserResponseDto> findAllUsers();
    Optional<UserResponseDto> findUserById(Long id);
    UserResponseDto updateUser(UserRequestDto userRequestDto, Long id);
    void deleteById(Long id);

}
