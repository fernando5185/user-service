package com.ferave.userservice.application.port.output;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ferave.userservice.domain.model.User;

public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmailAndIdNot(String email, UUID id);

    boolean existsByUsernameAndIdNot(String username, UUID id);
}