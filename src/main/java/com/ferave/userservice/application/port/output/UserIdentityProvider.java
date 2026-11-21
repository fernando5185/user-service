package com.ferave.userservice.application.port.output;

import java.util.List;

import com.ferave.userservice.infrastructure.adapter.input.dto.UserRequestDto;
import com.ferave.userservice.infrastructure.adapter.input.dto.UserResponseDto;

public interface UserIdentityProvider {

    UserResponseDto saveUserIdentity(UserRequestDto userIdentityRequest);

    List<UserResponseDto> usersIdentity();

    UserResponseDto getUserIdentityById(String keycloakId);

    void deleteUserIdentityById(String keycloakId);
}
