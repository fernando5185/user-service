package com.ferave.userservice.oauth2.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ferave.userservice.exceptions.NotFoundException;
import com.ferave.userservice.user.entity.User;
import com.ferave.userservice.user.repository.UserRepository;

@Service 
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username);
        if (user == null) throw new NotFoundException("User not found");
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUserName())
                .password("{noop}" + user.getPassword())
                .roles(user.getRoles().toString())
                .build();
    }
    
}
