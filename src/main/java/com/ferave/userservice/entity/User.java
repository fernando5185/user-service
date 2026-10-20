package com.ferave.userservice.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(
            name = "user_id",
            nullable = false
        ),
        inverseJoinColumns = @JoinColumn(
            name = "role_id",
            nullable = false
        ),
        uniqueConstraints = @UniqueConstraint (
            columnNames = {"user_id", "role_id"}
        )
    )
    private Set<Role> roles = new HashSet<>();
}