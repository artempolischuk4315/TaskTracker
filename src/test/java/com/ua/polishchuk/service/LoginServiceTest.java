package com.ua.polishchuk.service;

import com.ua.polishchuk.dto.UserDto;
import com.ua.polishchuk.entity.User;
import com.ua.polishchuk.repository.UserRepository;
import com.ua.polishchuk.service.mapper.impl.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    private static final String TEST_MAIL = "test@mail";

    private static final User user = getUser();
    private static final UserDto userDto = getUserDto();

    @InjectMocks
    LoginService systemUnderTest;

    @Mock
    UserRepository userRepository;

    @Mock
    UserMapper userMapper;

    @Test
    void loadByUsernameShouldReturnUserDtoIfUsernameIsCorrect() {
        when(userRepository.findByEmail(TEST_MAIL)).thenReturn(Optional.of(user));
        when(userMapper.mapEntityToDto(user)).thenReturn(userDto);

        UserDto userDtoActual = (UserDto) systemUnderTest.loadUserByUsername(TEST_MAIL);

        assertEquals(userDto, userDtoActual);
    }

    @Test
    void loadByUsernameShouldThrowUsernameNotFoundExceptionIfUsernameIsNotCorrect() {
        when(userRepository.findByEmail(TEST_MAIL)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> systemUnderTest.loadUserByUsername(TEST_MAIL));
    }

    private static User getUser() {
        return User.builder()
                .email(TEST_MAIL)
                .build();
    }

    private static UserDto getUserDto() {
        return UserDto.builder()
                .email(TEST_MAIL)
                .build();
    }
}