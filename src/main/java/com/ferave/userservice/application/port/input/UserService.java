package com.ferave.userservice.application.port.input;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ferave.userservice.infrastructure.adapter.input.dto.UserRequestDto;
import com.ferave.userservice.infrastructure.adapter.input.dto.UserResponseDto;
import com.ferave.userservice.infrastructure.adapter.input.dto.UserUpdateDto;

public interface UserService {    

    UserResponseDto saveUser(UserRequestDto userRequestDto);

    UserResponseDto registerUser(UserRequestDto userRequestDto);

    Page<UserResponseDto> users(Pageable pageable);

    UserResponseDto findUserById(UUID id);

    UserResponseDto updateUser(UserUpdateDto userUpdateDto, UUID id);

    void deleteUserById(UUID id);

}
