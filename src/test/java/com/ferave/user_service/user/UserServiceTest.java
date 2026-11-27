package com.ferave.user_service.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.ferave.userservice.user.dto.UserResponseDto;
import com.ferave.userservice.user.entity.User;
import com.ferave.userservice.user.mapper.UserMapper;
import com.ferave.userservice.user.repository.UserRepository;
import com.ferave.userservice.user.service.UserServiceImpl;

@ExtendWith (MockitoExtension.class)
class UserServiceTest {

    @Mock 
    UserRepository userRepository;

    @Mock   
    UserMapper userMapper;

    @InjectMocks 
    UserServiceImpl userServiceImpl;

    private UUID id;
    private User user;
    private UserResponseDto expectedResponse;

    @BeforeEach
    void setUp() {

        id = UUID.randomUUID();

        user = new User();
        user.setId(id);
        user.setKeycloakId("6940996881");
        user.setEmail("mateo@email.com");
        user.setUsername("mateo");
        user.setEnabled(true);

        expectedResponse = new UserResponseDto(
            id,
            "6940996881",
            "mateo",
            "mateo@email.com",
            true
        );
    }

    @Test 
    void findUserByIdTest() {
        // Arrange
        when(userRepository.findById(id))
            .thenReturn(Optional.of(user));

        when(userMapper.toDto(user))
            .thenReturn(expectedResponse);

        // Act
        UserResponseDto response = userServiceImpl.findUserById(id);

        // Assert
        assertNotNull(response);
        assertEquals(expectedResponse, response);

        verify(userRepository, times(1)).findById(id);
        verify(userMapper).toDto(user);

    }

    @Test
    void findUsersTest() {

        // Arrange
        Pageable pageable = PageRequest.of(0, 10);

        Page<User> userPage = new PageImpl<>(
            List.of(user),
            pageable,
            1
        );

        Page<UserResponseDto> expectedPage = new PageImpl<>(
            List.of(expectedResponse),
            pageable,
            1
        );

        when(userRepository.findAll(pageable))
            .thenReturn(userPage);

        when(userMapper.toDtoList(userPage))
            .thenReturn(expectedPage);

        // Act
        Page<UserResponseDto> response =
            userServiceImpl.users(pageable);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
        assertEquals(expectedPage, response);
        assertEquals(expectedResponse, response.getContent().get(0));

        // Verify
        verify(userRepository).findAll(pageable);
        verify(userMapper).toDtoList(userPage);
    }

    @Test 
    void setUpTest() {
        // Arrange


        // Act


        // Assert

        
    }
}
