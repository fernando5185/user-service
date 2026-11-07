package com.ferave.userservice.service;

import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ferave.userservice.dto.UserRequestDto;
import com.ferave.userservice.exceptions.KeycloakAdminException;

import jakarta.ws.rs.core.Response;

@Service 
public class KeycloakUserService {

    private final Keycloak keycloak;
    
    public KeycloakUserService(Keycloak keycloak) {
        this.keycloak = keycloak;
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

        try (Response response = userResource()
            .create(userRepresentation)) {

            int status = response.getStatus();

            if (status != Response.Status.CREATED.getStatusCode()) {

                throw new KeycloakAdminException("Error creating the user in Keycloak");

            }

        return CreatedResponseUtil.getCreatedId(response);
        
        }
    }

    public UserRepresentation findUserById(String id) {
        return userResource()
            .get(id)
            .toRepresentation();
    }

    public void deleteUserById (String id) {
        userResource()
            .get(id)
            .remove();
    }
}
