package com.ferave.userservice.integration.port;

import java.util.List;

import com.ferave.userservice.dto.UserRequestDto;
import com.ferave.userservice.dto.UserResponseDto;

public interface UserIdentityProvider {

    UserResponseDto saveUserIdentity(UserRequestDto userIdentityRequest);

    List<UserResponseDto> usersIdentity();

    UserResponseDto getUserIdentityById(String keycloakId);

    void deleteUserIdentityById(String keycloakId);
}
