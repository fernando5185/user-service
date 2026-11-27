package com.ferave.userservice.identity.port;

import java.util.List;

import com.ferave.userservice.identity.dto.UserIdentityRequestDto;
import com.ferave.userservice.identity.dto.UserIdentityResponseDto;

public interface UserIdentityProvider {

    UserIdentityResponseDto saveUserIdentity(UserIdentityRequestDto userIdentityRequest);

    List<UserIdentityResponseDto> usersIdentity();

    UserIdentityResponseDto getUserIdentityById(String keycloakId);

    void deleteUserIdentityById(String keycloakId);
}
