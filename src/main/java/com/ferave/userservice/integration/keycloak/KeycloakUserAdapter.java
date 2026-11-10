package com.ferave.userservice.integration.keycloak;

import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ferave.userservice.exceptions.KeycloakAdminException;
import com.ferave.userservice.integration.keycloak.mapper.KeycloakUserMapper;
import com.ferave.userservice.integration.model.UserIdentityRequest;
import com.ferave.userservice.integration.model.UserIdentityResponse;
import com.ferave.userservice.integration.port.UserIdentityProvider;

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
    public UserIdentityResponse saveUserIdentity(UserIdentityRequest userIdentityRequest) {
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

                throw new KeycloakAdminException(
                    "Keycloak returned status: " + status
                    + " - " + responseBody
                );
            }

            String keycloakId = CreatedResponseUtil.getCreatedId(response);

            UserRepresentation userRepresentationEntity = userIdentityMapper.toEntity(userIdentityRequest);

            userRepresentationEntity.setId(keycloakId);

            return userIdentityMapper.toDto(userRepresentationEntity);
        }
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
