package com.ferave.userservice.integration.keycloak;

import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;

import com.ferave.userservice.dto.UserRequestDto;
import com.ferave.userservice.exceptions.KeycloakAdminException;
import com.ferave.userservice.integration.keycloak.mapper.KeycloakUserMapper;
import com.ferave.userservice.integration.model.UserIdentityRequest;
import com.ferave.userservice.integration.model.UserIdentityResponse;
import com.ferave.userservice.integration.port.UserIdentityProvider;

import jakarta.ws.rs.core.Response;

public class KeycloakUserAdapter implements UserIdentityProvider {

    private final Keycloak keycloak;
    private final KeycloakUserMapper userIdentityMapper;

    public KeycloakUserAdapter(Keycloak keycloak, KeycloakUserMapper userIdentityMapper, String realm) {
        this.keycloak = keycloak;
        this.userIdentityMapper = userIdentityMapper;
        this.realm = realm;
    }

    private UsersResource userResource() {
        return keycloak.realm(realm).users();
    }

    @Value ("${keycloak.realm}")
    private String realm;

    public String createUser (UserRequestDto dto) {
        
        UserRepresentation userRepresentation = new UserRepresentation();

        userRepresentation.setUsername(dto.userName());
        userRepresentation.setEmail(dto.email());
        userRepresentation.setEnabled(true);

        try (Response response = keycloak.realm(realm)
            .users()
            .create(userRepresentation)) {

            int status = response.getStatus();

            if (status != Response.Status.CREATED.getStatusCode()) {

                String responseBody = response.hasEntity()
                    ? response.readEntity(String.class)
                    : "No response body";

                throw new KeycloakAdminException(
                    "Keycloak returned status: " + status
                    + " - " + responseBody
                );
            }
        return CreatedResponseUtil.getCreatedId(response);
        }
    }

    @Override
    public UserIdentityResponse saveUserIdentity(UserIdentityRequest userIdentityRequest) {
        UserRepresentation userRepresentation = new UserRepresentation();

        userRepresentation.setUsername(userIdentityRequest.username());
        userRepresentation.setEmail(userIdentityRequest.email());
        userRepresentation.setEnabled(true);

        Response response = keycloak.realm(realm)
            .users()
            .create(userRepresentation);

        CreatedResponseUtil.getCreatedId(response);

        return userIdentityMapper.toDto(userRepresentation);
    }

    @Override
    public UserIdentityResponse getUserIdentityById(String keycloakId) {
        UserRepresentation userRepresentation = userResource().get(keycloakId).toRepresentation();
        return userIdentityMapper.toDto(userRepresentation);
    }

    @Override
    public void deleteUserIdentityById(String keycloakId) {
        userResource().get(keycloakId).remove();
    }
}
