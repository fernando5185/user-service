package com.ferave.userservice.identity.port;

import java.util.List;

import com.ferave.userservice.user.dto.UserRequestDto;
import com.ferave.userservice.user.dto.UserResponseDto;

public interface UserIdentityProvider {

    UserResponseDto saveUserIdentity(UserRequestDto userIdentityRequest);

    List<UserResponseDto> usersIdentity();

    UserResponseDto getUserIdentityById(String keycloakId);

    void deleteUserIdentityById(String keycloakId);
}
