package com.ferave.user_service.user.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ferave.user_service.exceptions.AlreadyExistsException;
import com.ferave.user_service.user.dto.UserRequestDto;
import com.ferave.user_service.user.dto.UserResponseDto;
import com.ferave.user_service.user.entity.User;
import com.ferave.user_service.user.mapper.UserMapper;
import com.ferave.user_service.user.repository.UserRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor 
@Service 
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto saveUser(UserRequestDto userRequestDto) {
        Optional<User> findByEmail = userRepository.findByEmail(userRequestDto.email());
        findByEmail.ifPresent(e -> {
            throw new AlreadyExistsException("Already " + userRequestDto.email() + " exists");
        });
        User entity = userMapper.toEntity(userRequestDto);
        User saveUser =userRepository.save(entity);
        return userMapper.toDto(saveUser);
    }

    @Override
    public List<UserResponseDto> findAllUsers() {
        List<User> findUsers = userRepository.findAll();
        return userMapper.toListDto(findUsers);
    }

    @Override
    public Optional<UserResponseDto> findUserById(Long id) {

        throw new UnsupportedOperationException("Unimplemented method 'findUserById'");
    }

    @Override
    public UserResponseDto updateUser(UserRequestDto userRequestDto, Long id) {

        throw new UnsupportedOperationException("Unimplemented method 'updateUser'");
    }

    @Override
    public void deleteById(Long id) {
        
        throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
    }
    
}
