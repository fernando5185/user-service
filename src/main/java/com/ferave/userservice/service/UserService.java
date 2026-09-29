package com.ferave.userservice.service;

import java.util.List;

import com.ferave.userservice.dto.UserRequestDto;
import com.ferave.userservice.dto.UserResponseDto;
import com.ferave.userservice.dto.UserUpdateDto;

public interface UserService {

    UserResponseDto saveUser(UserRequestDto userRequestDto);

    List<UserResponseDto> users();

    UserResponseDto findUserById(Long id);

    UserResponseDto updateUser(UserUpdateDto userUpdateDto, Long id);

    void deleteUserById(Long id);

}
