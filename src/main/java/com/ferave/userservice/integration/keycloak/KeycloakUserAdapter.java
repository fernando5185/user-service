package com.ferave.userservice.integration.keycloak;

import java.util.List;

import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ferave.userservice.dto.UserRequestDto;
import com.ferave.userservice.dto.UserResponseDto;
import com.ferave.userservice.exceptions.integration.KeycloakIntegrationException;
import com.ferave.userservice.integration.keycloak.mapper.KeycloakUserMapper;
import com.ferave.userservice.integration.port.UserIdentityProvider;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

@Service 
public class KeycloakUserAdapter implements UserIdentityProvider {

    private final Keycloak keycloak;
    private final KeycloakUserMapper userIdentityMapper;

    public KeycloakUserAdapter(Keycloak keycloak, KeycloakUserMapper userIdentityMapper) {
        this.keycloak = keycloak;
        this.userIdentityMapper = userIdentityMapper;
    }

    private UsersResource userResource() {
        return keycloak.realm(realm).users();
    }

    @Value ("${keycloak.realm}")
    private String realm;

    @Override
    @Transactional 
    public UserResponseDto saveUserIdentity(UserRequestDto userIdentityRequest) {
        UserRepresentation userRepresentation = new UserRepresentation();

        userRepresentation.setUsername(userIdentityRequest.username());
        userRepresentation.setEmail(userIdentityRequest.email());
        userRepresentation.setEnabled(true);

        try (Response response = keycloak.realm(realm)
            .users()
            .create(userRepresentation)) {

            int status = response.getStatus();

            if (status != Response.Status.CREATED.getStatusCode()) {

                String responseBody = response.hasEntity()
                    ? response.readEntity(String.class)
                    : "No response body";

                throw new KeycloakIntegrationException(
                    "Keycloak returned status: " + status
                    + " - " + responseBody
                );
            }

            String keycloakId = CreatedResponseUtil.getCreatedId(response);

            userRepresentation.setId(keycloakId);

            return userIdentityMapper.toDto(userRepresentation);
        }
    }

    @Override
    public UserResponseDto getUserIdentityById(String keycloakId) {
        UserRepresentation userRepresentation = userResource().get(keycloakId).toRepresentation();
        return userIdentityMapper.toDto(userRepresentation);
    }

    @Override
    @Transactional 
    public void deleteUserIdentityById(String keycloakId) {
        userResource().get(keycloakId).remove();
    }

    @Override
    public List<UserResponseDto> usersIdentity() {
        List<UserRepresentation> userRepresentations = userResource().list();
        return userIdentityMapper
            .toDtoList(userRepresentations);
    }
}
