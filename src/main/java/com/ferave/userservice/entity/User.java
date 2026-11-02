package com.ferave.userservice.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@AllArgsConstructor 
@NoArgsConstructor 
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid_id", nullable = false, updatable = false)
    private UUID id;

    @Column (
        name = "keycloak_id",
        unique = true,
        length = 36
    )
    private String keycloakId;

    @Column(
        name = "user_name",
        nullable = false, 
        unique = true,
        length = 50
    )
    private String userName;

    @Column(
        nullable = false, 
        unique = true,
        length = 250
    )
    private String email;

    @Column(
        nullable = false
    )
    private Boolean enabled = true;
}