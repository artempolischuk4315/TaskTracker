package com.ua.polishchuk.service;

import com.ua.polishchuk.dto.UserFieldsToUpdate;
import com.ua.polishchuk.entity.Role;
import com.ua.polishchuk.entity.User;
import com.ua.polishchuk.repository.UserRepository;
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

    private final BCryptPasswordEncoder encoder;
    private final UserRepository userRepository;
    private final TaskService taskService;

    @Autowired
    public UserService(BCryptPasswordEncoder encoder, UserRepository userRepository, TaskService taskService) {
        this.encoder = encoder;
        this.userRepository = userRepository;
        this.taskService = taskService;
    }

    public Page<User> findAll(Pageable pageable){
        return userRepository
                .findAll(pageable);
    }

    public User findById(Integer id){
        return userRepository
                .findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public User findByEmail(String email){
        return userRepository
                .findByEmail(email).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public User save(User user){
        checkIfUserAlreadyRegistered(user);

        user = prepareEntityForSaving(user);

        return userRepository.save(user);
    }

    @Transactional
    public User update(UserFieldsToUpdate fieldsToUpdate, Integer userId){
        User userToUpdate = setParametersOfUpdatedUser(getUserIfExists(userId), fieldsToUpdate);

        return userRepository.save(userToUpdate);
    }

    @Transactional
    public void delete(Integer userId){
        User userToDelete = getUserIfExists(userId);

        deleteAllTasksLinkedWithUser(userId);

        userRepository.delete(userToDelete);
    }

    private void deleteAllTasksLinkedWithUser(Integer userId) {
        taskService
                .findSortedByUserFromNewToOld()
                .stream()
                .filter(task -> task.getUser().getId().equals(userId))
                .forEach(t -> taskService.delete(t.getId()));
    }

    private User prepareEntityForSaving(User userEntity) {
        userEntity.setPassword(encoder.encode(userEntity.getPassword()));

        if(userEntity.getRole()==null){
            userEntity.setRole(Role.ROLE_USER);
        }
        return userEntity;
    }

    private User setParametersOfUpdatedUser(User userToUpdate, UserFieldsToUpdate fieldsToUpdate){
        userToUpdate.setFirstName(fieldsToUpdate.getFirstName());
        userToUpdate.setLastName(fieldsToUpdate.getLastName());
        userToUpdate.setRole(Role.valueOf(fieldsToUpdate.getRole().toUpperCase()));

        return userToUpdate;
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
