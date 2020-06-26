package com.ua.polishchuk.service;

import com.ua.polishchuk.dto.UserDto;
import com.ua.polishchuk.entity.User;
import com.ua.polishchuk.repository.UserRepository;
import com.ua.polishchuk.service.mapper.EntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LoginService implements UserDetailsService {

    private final UserRepository userRepository;
    private final EntityMapper<User, UserDto> userMapper;

    @Autowired
    public LoginService(UserRepository userRepository, EntityMapper<User, UserDto> userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository
                .findByEmail(email)
                .map(userMapper::mapEntityToDto)
                .orElseThrow(() -> new UsernameNotFoundException(email));
    }
}
