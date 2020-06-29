package com.ua.polishchuk.facade;

import com.ua.polishchuk.dto.UserDto;
import com.ua.polishchuk.dto.UserFieldsToUpdate;
import com.ua.polishchuk.entity.Role;
import com.ua.polishchuk.entity.User;
import com.ua.polishchuk.service.UserService;
import com.ua.polishchuk.service.mapper.EntityMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserFacadeTest {

    private static final String FIRST_NAME = "F.Name";
    private static final String LAST_NAME = "L.Name";
    private static final Role ROLE = Role.ROLE_USER;
    private static final int ID = 1;
    private static final String EMAIL = "art4315@gmail.com";
    private static final String PASSWORD = "Pass";

    private static final UserDto userDto = getUserDto();
    private static final User user = getUser();
    private static final UserFieldsToUpdate userFieldsToUpdate = getFieldsToUpdate();

    @InjectMocks
    UserFacade systemUnderTest;

    @Mock
    UserService userService;

    @Mock
    EntityMapper<User, UserDto> mapper;

    @Test
    void findAllShouldReturnPageWithUsersDto(){
        Page<UserDto> expected = new PageImpl<>(Collections.singletonList(userDto));
        Page<User> notMappedList = new PageImpl<>(Collections.singletonList(user));
        Pageable pageable = PageRequest.of(1, 1);

        when(userService.findAll(pageable)).thenReturn(notMappedList);
        when(mapper.mapEntityToDto(user)).thenReturn(userDto);

        Page<UserDto> actual = systemUnderTest.findAll(pageable);

        assertEquals(expected, actual);
    }

    @Test
    void findByIdShouldReturnOkStatusWithUserDto(){
        when(userService.findById(ID)).thenReturn(user);
        when(mapper.mapEntityToDto(user)).thenReturn(userDto);

        UserDto actual = systemUnderTest.findById(ID);

        assertEquals(userDto, actual);
    }

    @Test
    void createShouldReturnUserDto(){
        when(userService.save(user)).thenReturn(user);
        when(mapper.mapEntityToDto(user)).thenReturn(userDto);
        when(mapper.mapDtoToEntity(userDto)).thenReturn(user);

        UserDto actual = systemUnderTest.save(userDto);

        assertEquals(userDto, actual);
    }

    @Test
    void updateShouldReturnUserDto(){
        when(userService.update(userFieldsToUpdate, user.getId())).thenReturn(user);
        when(mapper.mapEntityToDto(user)).thenReturn(userDto);

        UserDto actual = systemUnderTest.update(userFieldsToUpdate, user.getId());

        assertEquals(userDto, actual);
    }

    @Test
    void deleteShouldInvokeService(){
        systemUnderTest.delete(ID);

        verify(userService).delete(ID);
    }

    private static UserFieldsToUpdate getFieldsToUpdate(){
        return UserFieldsToUpdate.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .role(ROLE.toString())
                .build();
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
