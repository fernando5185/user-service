package com.ferave.userservice.mapper;


import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

import com.ferave.userservice.dto.UserRequestDto;
import com.ferave.userservice.dto.UserResponseDto;
import com.ferave.userservice.dto.UserUpdateDto;
import com.ferave.userservice.entity.User;

@Mapper(componentModel = "spring", 
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.WARN
) 
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "enabled", constant = "true")
    @Mapping(target = "keycloakId", constant = "true")
    User toEntity(UserRequestDto userRequestDto);

    UserResponseDto toDto(User user);

    default Page<UserResponseDto> toDtoList(Page<User> users){
        return users.map(this::toDto);
    };

    @BeanMapping(nullValuePropertyMappingStrategy = 
        NullValuePropertyMappingStrategy.IGNORE
    )
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "keycloakId", ignore = true)
    void updateUser(UserUpdateDto updateDto, @MappingTarget User entity);

}
 