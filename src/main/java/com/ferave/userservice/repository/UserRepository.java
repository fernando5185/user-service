package com.ferave.userservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ferave.userservice.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUserName(String userName);

    boolean existsByEmail(String email);

    boolean existsByUserName(String username);

    boolean existsByEmailAndIdNot(String email, Long id);

    boolean existsByUserNameAndIdNot(String userName, Long id);
}