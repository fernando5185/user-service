package com.ferave.userservice.integration.keycloak.mapper;

import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import com.ferave.userservice.integration.model.UserIdentityRequest;
import com.ferave.userservice.integration.model.UserIdentityResponse;

@Mapper (componentModel = "spring", 
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.WARN
) 
public interface KeycloakUserMapper {
    
    @Mapping (target = "externalId", ignore = true)
    UserIdentityResponse toDto(UserRepresentation userRepresentation);

    @Mapping (target = "id", ignore = true)
    @Mapping (target = "enabled", constant = "true")
    @Mapping (target = "emailVerified", constant = "true")
    @Mapping (target = "attributes", ignore = true)
    @Mapping (target = "requiredActions", ignore = true)
    @Mapping (target = "federatedIdentities", ignore = true)
    @Mapping (target = "serviceAccountClientId", ignore = true)
    @Mapping (target = "notBefore", ignore = true)
    @Mapping (target = "access", ignore = true)
    @Mapping (target = "disableableCredentialTypes", ignore = true)
    @Mapping (target = "clientRoles", ignore = true)
    @Mapping (target = "realmRoles", ignore = true)
    @Mapping (target = "groups", ignore = true)
    @Mapping (target = "credentials", ignore = true)
    @Mapping (target = "firstName", ignore = true)
    @Mapping (target = "lastName", ignore = true)
    @Mapping (target = "userProfileMetadata",  ignore = true)
    @Mapping(target = "self", ignore = true)
    @Mapping(target = "createdTimestamp", ignore = true)
    @Mapping(target = "totp", ignore = true)
    @Mapping(target = "socialLinks", ignore = true)
    @Mapping(target = "clientConsents", ignore = true)
    @Mapping(target = "verifiableCredentials", ignore = true)
    @Mapping(target = "issuedVerifiableCredentials", ignore = true)
    @Mapping(target = "federationLink", ignore = true)
    @Mapping(target = "origin", ignore = true)
    @Mapping(target = "rawAttributes", ignore = true)
    @Mapping(target = "applicationRoles", ignore = true)
    UserRepresentation toEntity(UserIdentityRequest userIdentityRequest);
}
