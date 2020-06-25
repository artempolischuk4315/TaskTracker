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

    public Page<UserDto> findAll(Pageable pageable){
        return userRepository
                .findAll(pageable)
                .map(mapper::mapEntityToDto);
    }

    public UserDto findById(Integer id){
        return mapper.mapEntityToDto(userRepository
                .findById(id).orElseThrow((EntityNotFoundException::new)));
    }

    public UserDto save(UserDto userDto){

        checkIfUserAlreadyRegistered(userDto);

        User userEntity = prepareEntityForSaving(userDto);

        return mapper.mapEntityToDto(userRepository.save(userEntity));
    }

    private User prepareEntityForSaving(UserDto userDto) {
        User userEntity = mapper.mapDtoToEntity(userDto);
        userEntity.setPassword(encoder.encode(userDto.getPassword()));

        if(userDto.getRole()==null){
            userEntity.setRole(Role.USER);
        }
        return userEntity;
    }

    public UserDto update(UpdateUserDto updateUserDto, Integer userId){

        User user = setParametersOfUpdatedUser(getUserIfExists(userId), updateUserDto);

        return mapper.mapEntityToDto(userRepository.save(user));
    }

    public void delete(Integer userId){

        getUserIfExists(userId);

        userRepository.deleteById(userId);
    }

    private User setParametersOfUpdatedUser(User user, UpdateUserDto updateUserDto){

        return User.builder()
                .role(Role.valueOf(updateUserDto.getRole().toUpperCase()))
                .firstName(updateUserDto.getFirstName())
                .lastName(updateUserDto.getLastName())
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }

    private User getUserIfExists(Integer userId) {

        Optional <User> user = userRepository.findById(userId);
        if(!user.isPresent()){
            throw new EntityNotFoundException(USER_NOT_PRESENT);
        }
        return user.get();
    }

    private void checkIfUserAlreadyRegistered(UserDto userDto) {
        userRepository.findByEmail(userDto.getEmail()).ifPresent(u -> {
            throw new EntityExistsException(USER_ALREADY_REGISTERED + userDto.getEmail());
        });
    }
}
