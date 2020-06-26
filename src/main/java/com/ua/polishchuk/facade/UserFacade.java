package com.ua.polishchuk.facade;

import com.ua.polishchuk.dto.UserDto;
import com.ua.polishchuk.dto.UserFieldsToUpdate;
import com.ua.polishchuk.entity.User;
import com.ua.polishchuk.service.UserService;
import com.ua.polishchuk.service.mapper.EntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class UserFacade {

    private final UserService userService;
    private final EntityMapper<User, UserDto> mapper;

    @Autowired
    public UserFacade(UserService userService, EntityMapper<User, UserDto> mapper) {
        this.userService = userService;
        this.mapper = mapper;
    }

    public Page<UserDto> findAll(Pageable pageable){
        return userService
                .findAll(pageable)
                .map(mapper::mapEntityToDto);
    }

    public UserDto findById(Integer userId){
        return mapper.mapEntityToDto(userService.findById(userId));
    }

    public UserDto save(UserDto userDto){

        return mapper.mapEntityToDto(userService.save(mapper.mapDtoToEntity(userDto)));
    }

    public UserDto update(UserFieldsToUpdate fieldsToUpdate, Integer id){

        return mapper.mapEntityToDto(userService.update(fieldsToUpdate, id));
    }

    public void delete(Integer id){
        userService.delete(id);
    }
}
