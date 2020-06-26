package com.ua.polishchuk.service;

import com.ua.polishchuk.dto.UpdateUserDto;
import com.ua.polishchuk.dto.UserDto;
import com.ua.polishchuk.entity.Role;
import com.ua.polishchuk.entity.User;
import com.ua.polishchuk.repository.UserRepository;
import com.ua.polishchuk.service.mapper.EntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UserService {

    private static final String USER_ALREADY_REGISTERED = "User already registered with provided email ";
    private static final String USER_NOT_PRESENT =  "User doesn't exists";

    private final EntityMapper<User, UserDto> mapper;
    private final BCryptPasswordEncoder encoder;
    private final UserRepository userRepository;

    @Autowired
    public UserService(EntityMapper<User, UserDto> mapper, BCryptPasswordEncoder encoder, UserRepository userRepository) {
        this.mapper = mapper;
        this.encoder = encoder;
        this.userRepository = userRepository;
    }

    public Page<User> findAll(Pageable pageable){
        return userRepository
                .findAll(pageable);
    }

    public User findById(Integer id){
        return userRepository
                .findById(id).orElseThrow((EntityNotFoundException::new));
    }

    @Transactional
    public User save(User user){
        checkIfUserAlreadyRegistered(user);

        user = prepareEntityForSaving(user);

        return userRepository.save(user);
    }

    @Transactional
    public User update(UpdateUserDto updatedFields, Integer userId){
        User user = setParametersOfUpdatedUser(getUserIfExists(userId), updatedFields);

        return userRepository.save(user);
    }

    @Transactional
    public void delete(Integer userId){
        userRepository.delete(getUserIfExists(userId));
    }

    private User prepareEntityForSaving(User userEntity) {
        userEntity.setPassword(encoder.encode(userEntity.getPassword()));

        if(userEntity.getRole()==null){
            userEntity.setRole(Role.USER);
        }
        return userEntity;
    }

    private User setParametersOfUpdatedUser(User userToUpdate, UpdateUserDto updateUserDto){
        return User.builder()
                .role(Role.valueOf(updateUserDto.getRole().toUpperCase()))
                .firstName(updateUserDto.getFirstName())
                .lastName(updateUserDto.getLastName())
                .id(userToUpdate.getId())
                .email(userToUpdate.getEmail())
                .password(userToUpdate.getPassword())
                .build();
    }

    private User getUserIfExists(Integer userId) {
        Optional <User> user = userRepository.findById(userId);
        if(!user.isPresent()){
            throw new EntityNotFoundException(USER_NOT_PRESENT);
        }
        return user.get();
    }

    private void checkIfUserAlreadyRegistered(User user) {
        userRepository.findByEmail(user.getEmail()).ifPresent(u -> {
            throw new EntityExistsException(USER_ALREADY_REGISTERED + user.getEmail());
        });
    }
}
