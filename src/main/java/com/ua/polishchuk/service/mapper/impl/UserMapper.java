package com.ua.polishchuk.service.mapper.impl;


import com.ua.polishchuk.dto.UserDto;
import com.ua.polishchuk.entity.Role;
import com.ua.polishchuk.entity.User;
import com.ua.polishchuk.service.mapper.EntityMapper;
import org.springframework.stereotype.Component;


@Component
public class UserMapper implements EntityMapper<User, UserDto> {
    @Override
    public UserDto mapEntityToDto(User entity) {
        return UserDto.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .role(entity.getRole())
                .password(entity.getPassword())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .build();
    }

    @Override
    public User mapDtoToEntity(UserDto dto) {
        return User.builder()
                .id(dto.getId())
                .email(dto.getEmail())
                .role(dto.getRole())
                .password(dto.getPassword())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .build();
    }

}
