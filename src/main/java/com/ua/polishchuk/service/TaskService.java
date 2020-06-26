package com.ua.polishchuk.service;

import com.ua.polishchuk.dto.EditTaskDto;
import com.ua.polishchuk.dto.TaskDto;
import com.ua.polishchuk.entity.Task;
import com.ua.polishchuk.entity.TaskStatus;
import com.ua.polishchuk.repository.TaskRepository;
import com.ua.polishchuk.repository.UserRepository;
import com.ua.polishchuk.service.mapper.EntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private static final String TASK_ALREADY_EXISTS = "Task with this title already exists: ";
    private static final String TASK_NOT_EXISTS = "Task not exists with provided id";
    private static final String NOT_UNIQUE_TITLE = "Not unique title exception";

    private final EntityMapper<Task, TaskDto> mapper;
    private final TaskRepository taskRepository;
    private final UserService userService;

    @Autowired
    public TaskService(EntityMapper<Task, TaskDto> mapper,
                       TaskRepository taskRepository, UserService userService) {
        this.mapper = mapper;
        this.taskRepository = taskRepository;
        this.userService = userService;
    }

    @Transactional
    public Task save(Task task){
        checkIfTaskAlreadyExists(task.getTitle());

        Task taskToSave = prepareTaskForSaving(task);

        return taskRepository.save(taskToSave);
    }

    @Transactional
    public Task edit(Integer taskId, EditTaskDto fieldsToEdit){
        checkIfNewTitleIsUniqueRelativeToOtherTasks(fieldsToEdit.getTitle(), taskId);

        Task task = setParametersForEditingTask(getTaskIfExists(taskId), fieldsToEdit);

        return taskRepository.save(task);
    }

    @Transactional
    public Task changeStatus(Integer taskId, TaskStatus status){
        Task task = getTaskIfExists(taskId);
        task.setStatus(status);
        return taskRepository.save(task);
    }

    @Transactional
    public Task changeUser(Integer taskId, Integer userId){
        Task task = getTaskIfExists(taskId);

        task.setUser(userService.findById(userId));

        return taskRepository.save(task);
    }

    @Transactional
    public void delete(Integer taskId){
        taskRepository.delete(getTaskIfExists(taskId));
    }

    public List<Task> findByStatus(TaskStatus status){
        return taskRepository
                .findAll()
                .stream()
                .filter(task -> task.getStatus().equals(status))
                .collect(Collectors.toList());
    }

    public List<Task> findSortedByUserFromOldToNew(){
        return taskRepository.findAll()
                .stream()
                .sorted(Comparator.comparingInt(t -> t.getUser().getId()))
                .collect(Collectors.toList());
    }

    public List<Task> findSortedByUserFromNewToOld(){
        return taskRepository.findAll()
                .stream()
                .sorted((t1,t2) -> Integer.compare(t2.getUser().getId(), t1.getUser().getId()))
                .collect(Collectors.toList());
    }

    private void checkIfNewTitleIsUniqueRelativeToOtherTasks(String title, Integer id) {
        Optional<Task> task = taskRepository.findByTitle(title);

        if(task.isPresent() && !task.get().getId().equals(id)){
            throw new NonUniqueResultException(NOT_UNIQUE_TITLE);
        }
    }

    private Task getTaskIfExists(Integer taskId) {
        Optional<Task> task = taskRepository.findById(taskId);
        if(!task.isPresent()){
            throw new EntityNotFoundException(TASK_NOT_EXISTS);
        }
        return task.get();
    }

    private Task setParametersForEditingTask(Task task, EditTaskDto fieldsToEdit) {
        return Task.builder()
                .id(task.getId())
                .user(task.getUser())
                .status(task.getStatus())
                .title(fieldsToEdit.getTitle())
                .description(fieldsToEdit.getDescription())
                .build();
    }

    private Task prepareTaskForSaving(Task taskToSave){
        if(taskToSave.getStatus() == null){
            taskToSave.setStatus(TaskStatus.VIEW);
        }
        return taskToSave;
    }

    private void checkIfTaskAlreadyExists(String title){
        taskRepository.findByTitle(title).ifPresent(t -> {
            throw new EntityExistsException(TASK_ALREADY_EXISTS + title);
        });
    }
}
