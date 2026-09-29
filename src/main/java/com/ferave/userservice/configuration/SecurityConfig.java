package com.ferave.userservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import com.ferave.userservice.security.KeycloakJwtAuthenticationConverter;

@Configuration 
@EnableWebSecurity 
@EnableMethodSecurity (prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
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

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/v1/user/entities"
                )
                    .hasAnyRole("USER", "ADMIN")

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/v1/user/**"
                )
                    .hasAnyRole("USER", "ADMIN")

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/v1/user"
                )
                    .hasRole("ADMIN")

                .requestMatchers(
                    HttpMethod.PATCH,
                    "/api/v1/user/**"
                )
                    .hasRole("ADMIN")

                .requestMatchers(
                    HttpMethod.DELETE,
                    "/api/v1/user/**"
                )
                    .hasRole("ADMIN")

                .anyRequest()
                    .authenticated()
            )

            .oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwt ->
                    jwt.jwtAuthenticationConverter(
                        new KeycloakJwtAuthenticationConverter()
                    )
                )
            );

        return http.build();
    }
}