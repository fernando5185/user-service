package com.ferave.userservice.security;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public class KeycloakJwtAuthenticationConverter 
    implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {

        Map<String, Object> realmAccess =
                jwt.getClaim("realm_access");

        Collection<SimpleGrantedAuthority> authorities =
                Collections.emptyList();

        if (realmAccess != null) {

            Object roles = realmAccess.get("roles");

            if (roles instanceof Collection<?> roleCollection) {

                authorities = roleCollection.stream()
                        .map(Object::toString)
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                        .toList();
            }
        }

        return new JwtAuthenticationToken(jwt, authorities);
    }
}
