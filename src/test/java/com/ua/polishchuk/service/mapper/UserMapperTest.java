package com.ua.polishchuk.service.mapper;

import com.ua.polishchuk.dto.UserDto;
import com.ua.polishchuk.entity.Role;
import com.ua.polishchuk.entity.User;
import com.ua.polishchuk.service.mapper.impl.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class UserMapperTest {

    private static final String FIRST_NAME = "F.Name";
    private static final String LAST_NAME = "L.Name";
    private static final Role ROLE = Role.ROLE_USER;
    private static final int ID = 1;
    private static final String EMAIL = "art4315@gmail.com";
    private static final String PASSWORD = "Pass";

    private static final UserDto userDto = getUserDto();
    private static final User user = getUser();

    private final EntityMapper<User, UserDto> mapper = new UserMapper();

    @Test
    void mapEntityToDto_ShouldReturnDto() {
        UserDto actual = mapper.mapEntityToDto(user);

        assertEquals(userDto, actual);
    }

    @Test
    void mapDtoToEntity_ShouldReturnEntity() {
        User actual = mapper.mapDtoToEntity(userDto);

        assertEquals(user, actual);
    }

    private static UserDto getUserDto(){
        return UserDto.builder()
                .id(ID)
                .email(EMAIL)
                .password(PASSWORD)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .role(ROLE)
                .build();
    }
    private static User getUser(){
        return User.builder()
                .id(ID)
                .role(ROLE)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .password(PASSWORD)
                .build();
    }
}
