package com.ferave.user_service.user.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.ferave.user_service.user.dto.UserRequestDto;
import com.ferave.user_service.user.dto.UserResponseDto;
import com.ferave.user_service.user.dto.UserUpdateDto;
import com.ferave.user_service.user.entity.User;

@Mapper(componentModel = "spring", 
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
) 
public interface UserMapper {
    UserResponseDto toDto(User user);

    @Mapping (target = "roles", ignore = true)
    @Mapping (target = "id", ignore = true)
    User toEntity(UserRequestDto userRequestDto);

    List<UserResponseDto> toListDto(List<User> users);

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    void updateUser(UserUpdateDto updateDto, @MappingTarget User entity);
}
