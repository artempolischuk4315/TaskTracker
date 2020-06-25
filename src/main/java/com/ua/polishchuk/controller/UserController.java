package com.ua.polishchuk.controller;

import com.ua.polishchuk.dto.UpdateUserDto;
import com.ua.polishchuk.dto.UserDto;
import com.ua.polishchuk.entity.Role;
import com.ua.polishchuk.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private static final String WRONG_ROLE_TYPE = "Wrong role type";

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/registration")
    public ResponseEntity<Object> create(
            @Valid @RequestBody UserDto userDto, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            Map<String, Object> errors = getAllErrorsList(bindingResult);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.save(userDto));
    }

    @GetMapping("")
    public ResponseEntity<Page<UserDto>> readAll(@PageableDefault(size = 10,
            sort = "id", direction = Sort.Direction.ASC) Pageable pageable){

        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> read(@PathVariable Integer id){

        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.findById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> update(
            @Valid @RequestBody UpdateUserDto userDto,
                BindingResult bindingResult, @PathVariable Integer id){

        if(!Role.contains(userDto.getRole())){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST).body(WRONG_ROLE_TYPE);
        }

        if(bindingResult.hasErrors()){
            Map<String, Object> body = getAllErrorsList(bindingResult);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.update(userDto, id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Integer id){

        userService.delete(id);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    private Map<String, Object> getAllErrorsList(BindingResult bindingResult) {

        return bindingResult.getFieldErrors()
                .stream()
                .collect(Collectors.toMap(FieldError::getField,
                        DefaultMessageSourceResolvable::getDefaultMessage));
    }
}
