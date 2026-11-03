package com.ferave.userservice.configuration;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration 
public class KeycloakAdminConfig {
    
    @Bean (destroyMethod="close")
    public Keycloak keycloak(
        @org.springframework.beans.factory.annotation.Value ("${keycloak.server-url}") String serverUrl,
        @org.springframework.beans.factory.annotation.Value ("${keycloak.realm}") String realm,
        @org.springframework.beans.factory.annotation.Value ("${keycloak.client-id}") String clientId,
        @org.springframework.beans.factory.annotation.Value ("${keycloak.client-secret}") String clientSecret
    ){
        return KeycloakBuilder.builder()
            .serverUrl(serverUrl)
            .realm(realm)
            .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
            .clientId(clientId)
            .clientSecret(clientSecret)
            .build();
    }
}
