package com.ferave.userservice.user.dto;

public record UserRequestDto (String userName, String email, String password, Boolean enabled) {}