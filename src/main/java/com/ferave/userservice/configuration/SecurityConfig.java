package com.ferave.userservice.configuration;

import java.util.ArrayList;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import java.util.Collection;
import java.util.Map;

@Configuration 
@EnableWebSecurity 
@EnableMethodSecurity (prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            )

            .authorizeHttpRequests(auth -> auth

                .requestMatchers("/public/**")
                .permitAll()

                .anyRequest()
                .authenticated()
            )

            .oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwt ->
                    jwt.jwtAuthenticationConverter(
                        jwtAuthenticationConverter()
                    )
                )
            );

        return http.build();
    }

    @Bean
    public Converter<Jwt, ? extends AbstractAuthenticationToken>
    jwtAuthenticationConverter() {

        return jwt -> {

            Collection<GrantedAuthority> authorities =
                new ArrayList<>();

            Map<String, Object> realmAccess =
                jwt.getClaim("realm_access");

            if (realmAccess != null) {

                Object roles =
                    realmAccess.get("roles");

                if (roles instanceof Collection<?> roleCollection) {

                    roleCollection.forEach(role -> {

                        authorities.add(
                            new SimpleGrantedAuthority(
                                "ROLE_" + role.toString()
                            )
                        );

                    });
                }
            }

            return new JwtAuthenticationToken(
                jwt,
                authorities
            );
        };
    }
}