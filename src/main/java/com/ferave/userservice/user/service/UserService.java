package com.ferave.userservice.user.service;

import java.util.List;
import java.util.Optional;

import com.ferave.userservice.user.dto.UserRequestDto;
import com.ferave.userservice.user.dto.UserResponseDto;
import com.ferave.userservice.user.dto.UserUpdateDto;

public interface UserService {

    UserResponseDto savedUser(UserRequestDto userRequestDto);
    List<UserResponseDto> users();
    Optional<UserResponseDto> findUserById(Long id);
    UserResponseDto updatedUser(UserUpdateDto userUpdateDto, Long id);
    void deleteUserById(Long id);

}
