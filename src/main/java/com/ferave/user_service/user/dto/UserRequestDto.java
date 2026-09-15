package com.ferave.user_service.user.dto;

public record UserRequestDto (String userName, String email, String password, Boolean enabled) {}