package com.ferave.userservice.identity.keycloak;

import java.util.List;

import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ferave.userservice.identity.dto.UserIdentityRequestDto;
import com.ferave.userservice.identity.dto.UserIdentityResponseDto;
import com.ferave.userservice.identity.keycloak.mapper.KeycloakUserMapper;
import com.ferave.userservice.identity.port.UserIdentityProvider;
import com.ferave.userservice.shared.exceptions.integration.KeycloakIntegrationException;

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
    public UserIdentityResponseDto saveUserIdentity(UserIdentityRequestDto userIdentityRequest) {
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
    public UserIdentityResponseDto getUserIdentityById(String keycloakId) {
        UserRepresentation userRepresentation = userResource().get(keycloakId).toRepresentation();
        return userIdentityMapper.toDto(userRepresentation);
    }

    @Override
    @Transactional 
    public void deleteUserIdentityById(String keycloakId) {
        userResource().get(keycloakId).remove();
    }

    @Override
    public List<UserIdentityResponseDto> usersIdentity() {
        List<UserRepresentation> userRepresentations = userResource().list();
        return userIdentityMapper
            .toDtoList(userRepresentations);
    }
}
