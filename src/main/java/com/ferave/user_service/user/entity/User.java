package com.ferave.user_service.user.entity;

import java.util.HashSet;
import java.util.Set;

import com.ferave.user_service.role.Role;

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
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@AllArgsConstructor 
@NoArgsConstructor 
@Entity
@Table(name = "user-service")
public class User {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY) 
    private Long id;

    @Column (unique = true, nullable = false)
    private String userName;
    
    @Email 
    @Column (unique = true, nullable = false)
    private String email;
    
    private String password;
    
    @Column (nullable = false)
    private Boolean enabled;

    @ManyToMany (fetch = FetchType.EAGER)
    @JoinTable (
        name = "user_roles",
        joinColumns = @JoinColumn (name = "user_id"),
        inverseJoinColumns = @JoinColumn (name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
}