package com.ferave.userservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ferave.userservice.dto.UserRequestDto;
import com.ferave.userservice.dto.UserResponseDto;
import com.ferave.userservice.dto.UserUpdateDto;

public interface UserService {

    UserResponseDto saveUser(UserRequestDto userRequestDto);

    Page<UserResponseDto> users(Pageable pageable);

    UserResponseDto findUserById(Long id);

    UserResponseDto updateUser(UserUpdateDto userUpdateDto, Long id);

    void deleteUserById(Long id);

}
