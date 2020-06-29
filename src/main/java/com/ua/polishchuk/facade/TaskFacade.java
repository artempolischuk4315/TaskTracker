package com.ua.polishchuk.facade;

import com.ua.polishchuk.dto.TaskDto;
import com.ua.polishchuk.dto.TaskFieldsToEdit;
import com.ua.polishchuk.entity.Task;
import com.ua.polishchuk.entity.TaskStatus;
import com.ua.polishchuk.entity.User;
import com.ua.polishchuk.service.TaskService;
import com.ua.polishchuk.service.UserService;
import com.ua.polishchuk.service.mapper.EntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TaskFacade {

    private static final String ASC = "asc";

    private final TaskService taskService;
    private final UserService userService;
    private final EntityMapper<Task, TaskDto> mapper;

    @Autowired
    public TaskFacade(TaskService taskService, UserService userService, EntityMapper<Task, TaskDto> mapper) {
        this.taskService = taskService;
        this.userService = userService;
        this.mapper = mapper;
    }

    public List<TaskDto> findSortedByUser(String order){
        return order.equals(ASC) ?
                findSortedByUserFromOldToNew() : findSortedByUserFromNewToOld();
    }

    public List<TaskDto> findFilteredByStatus(TaskStatus status){
        return taskService.findByStatus(status)
                .stream()
                .map(mapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    public TaskDto save(TaskDto taskDto, Principal principal){
        Task taskToCreate = mapper.mapDtoToEntity(taskDto);

        User taskOwnerUser = userService.findByEmail(principal.getName());

        return mapper.mapEntityToDto(taskService.save(taskToCreate, taskOwnerUser));
    }

    public TaskDto edit(Integer taskId, TaskFieldsToEdit editTaskDto){
        return mapper.mapEntityToDto(taskService.edit(taskId, editTaskDto));
    }

    public TaskDto changeStatus(Integer taskId, TaskStatus status){
        return mapper.mapEntityToDto(taskService.changeStatus(taskId, status));
    }

    public TaskDto changeUser(Integer taskId, Integer userId){

        User userToSet = userService.findById(userId);

        return mapper.mapEntityToDto(taskService.changeUser(taskId, userToSet));
    }

    public void delete(Integer taskId){
        taskService.delete(taskId);
    }

    private List<TaskDto> findSortedByUserFromOldToNew(){
        return taskService
                .findSortedByUserFromOldToNew()
                .stream()
                .map(mapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    private List<TaskDto> findSortedByUserFromNewToOld(){
        return taskService
                .findSortedByUserFromNewToOld()
                .stream()
                .map(mapper::mapEntityToDto)
                .collect(Collectors.toList());
    }
}
