package com.ferave.userservice.user.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ferave.userservice.user.dto.UserRequestDto;
import com.ferave.userservice.user.dto.UserResponseDto;
import com.ferave.userservice.user.dto.UserUpdateDto;

public interface UserService {    

    UserResponseDto saveUser(UserRequestDto userRequestDto);

    Page<UserResponseDto> users(Pageable pageable);

    UserResponseDto findUserById(UUID id);

    UserResponseDto updateUser(UserUpdateDto userUpdateDto, UUID id);

    void deleteUserById(UUID id);

}
