package com.ferave.userservice.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ferave.userservice.entity.User;

public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByEmail(String email);

    boolean existsByUserName(String username);

    boolean existsByEmailAndIdNot(String email, UUID id);

    boolean existsByUserNameAndIdNot(String userName, UUID id);
}