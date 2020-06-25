package com.ua.polishchuk.service;

import com.ua.polishchuk.dto.UserDto;
import com.ua.polishchuk.entity.Role;
import com.ua.polishchuk.entity.User;
import com.ua.polishchuk.repository.UserRepository;
import com.ua.polishchuk.service.mapper.EntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<UserDto> findAll(Pageable pageable){
        return userRepository.findAll(pageable)
                .stream()
                .map(mapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    public UserDto findById(Integer id){
        return mapper.mapEntityToDto(userRepository
                .findById(id).orElseThrow((EntityNotFoundException::new)));
    }

    public UserDto findByEmail(String email){
        return mapper.mapEntityToDto(userRepository
                .findByEmail(email).orElseThrow((EntityNotFoundException::new)));
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

    public UserDto update(UserDto userDto, Integer userId){

        checkIfUserExists(userId);

        User user = mapper.mapDtoToEntity(userDto);

        return mapper.mapEntityToDto(userRepository.save(user));
    }

    public void delete(Integer userId){

        checkIfUserExists(userId);

        userRepository.deleteById(userId);
    }

    private void checkIfUserExists(Integer userId) {
        if(!userRepository.findById(userId).isPresent()){
            throw new EntityExistsException(USER_NOT_PRESENT );
        }
    }

    private void checkIfUserAlreadyRegistered(UserDto userDto) {
        userRepository.findByEmail(userDto.getEmail()).ifPresent(u -> {
            throw new EntityExistsException(USER_ALREADY_REGISTERED + userDto.getEmail());
        });
    }
}
