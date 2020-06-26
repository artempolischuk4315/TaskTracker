package com.ua.polishchuk.controller;

import com.ua.polishchuk.dto.UserDto;
import com.ua.polishchuk.dto.UserFieldsToUpdate;
import com.ua.polishchuk.entity.Role;
import com.ua.polishchuk.facade.UserFacade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private static final String WRONG_ROLE_TYPE = "Wrong role type";
    private static final String FIRST_NAME = "F.Name";
    private static final String LAST_NAME = "L.Name";
    private static final Role ROLE = Role.ROLE_USER;
    private static final int ID = 1;
    private static final String EMAIL = "art4315@gmail.com";
    private static final String PASSWORD = "Pass";

    private static final UserDto userDto = getUserDto();
    private static final UserFieldsToUpdate userFieldsToUpdate = getFieldsToUpdate();
    private static final String WRONG_ROLE = "WRONG_ROLE";

    @InjectMocks
    UserController systemUnderTest;

    @Mock
    UserFacade userFacade;

    @Mock
    BindingResult bindingResult;

    @Test
    void createShouldReturnBadRequestIfRequestWrong(){
        when(bindingResult.hasErrors()).thenReturn(true);

        ResponseEntity<Object> response = systemUnderTest.create(userDto, bindingResult);

        HttpStatus actual = response.getStatusCode();
        HttpStatus expected = HttpStatus.BAD_REQUEST;

        assertEquals(expected, actual);
    }

    @Test
    void createShouldReturnOkWithSavedUserIfRequestCorrect(){
        when(bindingResult.hasErrors()).thenReturn(false);
        when(userFacade.save(userDto)).thenReturn(userDto);

        ResponseEntity<Object> actual = systemUnderTest.create(userDto, bindingResult);
        ResponseEntity<Object> expected = ResponseEntity.status(HttpStatus.OK).body(userDto);

        assertEquals(expected, actual);
    }

    @Test
    void readAllShouldReturnPageWithUsers(){
        Page<UserDto> expected = new PageImpl<>(Collections.singletonList(userDto));
        Pageable pageable = PageRequest.of(1, 1);

        when(userFacade.findAll(pageable)).thenReturn(expected);

        Page<UserDto> actual = systemUnderTest.readAll(pageable).getBody();

        assertEquals(expected, actual);
    }

    @Test
    void readShouldReturnOkStatusWithUser(){
        when(userFacade.findById(ID)).thenReturn(userDto);

        ResponseEntity<Object> actual = systemUnderTest.read(ID);
        ResponseEntity<Object> expected = ResponseEntity.status(HttpStatus.OK).body(userDto);

        assertEquals(expected, actual);
    }

    @Test
    void updateShouldReturnBadRequestStatusIfBindingHasErrors(){
        when(bindingResult.hasErrors()).thenReturn(true);

        ResponseEntity<Object> response = systemUnderTest.update(userFieldsToUpdate, bindingResult, ID);

        HttpStatus actual = response.getStatusCode();
        HttpStatus expected = HttpStatus.BAD_REQUEST;

        assertEquals(expected, actual);
    }

    @Test
    void updateShouldReturnBadRequestStatusIfRoleIsWrong(){
        when(bindingResult.hasErrors()).thenReturn(false);
        userFieldsToUpdate.setRole(WRONG_ROLE);

        ResponseEntity<Object> actual = systemUnderTest.update(userFieldsToUpdate, bindingResult, ID);
        ResponseEntity<Object> expected = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(WRONG_ROLE_TYPE);

        assertEquals(expected, actual);
    }

    @Test
    void updateShouldReturnOkStatusWithUserIfRequestCorrect(){
        when(bindingResult.hasErrors()).thenReturn(false);
        userFieldsToUpdate.setRole(ROLE.toString());
        when(userFacade.update(userFieldsToUpdate, ID)).thenReturn(userDto);

        ResponseEntity<Object> actual = systemUnderTest.update(userFieldsToUpdate, bindingResult, ID);
        ResponseEntity<Object> expected = ResponseEntity.status(HttpStatus.OK).body(userDto);

        assertEquals(expected, actual);
    }

    @Test
    void deleteShouldReturnOkStatus(){
        ResponseEntity<Void> actual = systemUnderTest.delete(ID);
        ResponseEntity<Void> expected = ResponseEntity.status(HttpStatus.OK).build();

        verify(userFacade).delete(ID);
        assertEquals(expected, actual);
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
}
