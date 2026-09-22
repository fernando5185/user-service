package com.ferave.userservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.web.SecurityFilterChain;

@Configuration 
@EnableWebSecurity 
@EnableMethodSecurity (prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {

    @Bean 
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) {
        return authenticationConfiguration.getAuthenticationManager();
    }
    
    @Bean 
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            .authorizeHttpRequests((authorize) -> authorize
                    .requestMatchers("/public/**")
                    .permitAll()
                    .anyRequest().authenticated()
            )
            .sessionManagement(session -> session 
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .cors(s -> s.disable())
            .csrf(s -> s.disable())
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
        return httpSecurity.build();
    }

    @Bean 
    public JwtDecoder jwtDecoder(){
        return JwtDecoders.fromIssuerLocation(null);
    }

    @Bean 
    public PasswordEncoder passwordEncoder () {
        return new BCryptPasswordEncoder();
    }
}
