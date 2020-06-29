package com.ua.polishchuk.service;

import com.ua.polishchuk.dto.UserFieldsToUpdate;
import com.ua.polishchuk.entity.Role;
import com.ua.polishchuk.entity.Task;
import com.ua.polishchuk.entity.User;
import com.ua.polishchuk.repository.UserRepository;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private static final String FIRST_NAME = "F.Name";
    private static final String LAST_NAME = "L.Name";
    private static final Role ROLE = Role.ROLE_USER;
    private static final Role ANOTHER_ROLE = Role.ROLE_ADMIN;
    private static final int ID = 1;
    private static final String EMAIL = "art4315@gmail.com";
    private static final String PASSWORD = "Pass";
    private static final String ANOTHER_FIRST_NAME = "Another F.Name";

    private static User user;
    private static final UserFieldsToUpdate userFieldsToUpdate = getFieldsToUpdate();

    @InjectMocks
    UserService systemUnderTest;

    @Mock
    UserRepository userRepository;

    @Mock
    TaskService taskService;

    @Mock
    BCryptPasswordEncoder encoder;

    @BeforeEach
    void setUp(){
        user = getUser();
    }

    @Test
    void findAllShouldReturnPageWithUsers(){
        Page<User> expected = new PageImpl<>(Collections.singletonList(user));
        Pageable pageable = PageRequest.of(1, 1);

        when(userRepository.findAll(pageable)).thenReturn(expected);

        Page<User> actual = systemUnderTest.findAll(pageable);

        assertEquals(expected, actual);
    }

    @Test
    void findByIdShouldThrowEntityNotFoundExceptionIfUserNotExists(){
        when(userRepository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> systemUnderTest.findById(ID));
    }

    @Test
    void findByIdShouldReturnUser(){
        when(userRepository.findById(ID)).thenReturn(Optional.of(user));

        User actual = systemUnderTest.findById(ID);

        assertEquals(user, actual);
    }

    @Test
    void findByEmailShouldThrowEntityNotFoundExceptionIfUserNotExists(){
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> systemUnderTest.findByEmail(EMAIL));
    }

    @Test
    void findByEmailShouldReturnUser(){
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));

        User actual = systemUnderTest.findByEmail(EMAIL);

        assertEquals(user, actual);
    }

    @Test
    void saveShouldThrowEntityExistsExceptionIfUserAlreadyRegistered(){
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));

        assertThrows(EntityExistsException.class, () -> systemUnderTest.save(user));
    }

    @Test
    void saveShouldReturnSavedUser(){

        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(user);
        when(encoder.encode(PASSWORD)).thenReturn(PASSWORD);

        user.setRole(null);
        User actual = systemUnderTest.save(user);

        verify(encoder).encode(PASSWORD);
        assertEquals(ROLE, user.getRole());
        assertEquals(user, actual);
    }

    @Test
    void updateShouldThrowEntityNotFoundExceptionIfUserNotExists(){
        when(userRepository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> systemUnderTest.update(userFieldsToUpdate, ID));
    }

    @Test
    void updateShouldReturnUpdatedUser(){
        when(userRepository.findById(ID)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        assertEquals(FIRST_NAME, user.getFirstName());
        assertEquals(ROLE, user.getRole());

        User actual = systemUnderTest.update(userFieldsToUpdate, ID);

        assertEquals(ANOTHER_FIRST_NAME, user.getFirstName());
        assertEquals(ANOTHER_ROLE, user.getRole());
        assertEquals(user, actual);
    }

    @Test
    void deleteShouldInvokeTaskAndUserServicesForDeleting(){
        when(taskService.findSortedByUserFromNewToOld())
                .thenReturn(Collections.singletonList(Task.builder().id(ID).user(user).build()));
        when(userRepository.findById(ID)).thenReturn(Optional.of(user));

        systemUnderTest.delete(ID);

        verify(userRepository).delete(user);
        verify(taskService).delete(ID);
        verify(taskService).findSortedByUserFromNewToOld();
    }

    private static UserFieldsToUpdate getFieldsToUpdate(){
        return UserFieldsToUpdate.builder()
                .firstName(ANOTHER_FIRST_NAME)
                .lastName(LAST_NAME)
                .role(ANOTHER_ROLE.toString())
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
