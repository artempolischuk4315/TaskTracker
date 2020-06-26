package com.ua.polishchuk.service.util;

import com.ua.polishchuk.dto.EditTaskDto;
import com.ua.polishchuk.dto.TaskDto;
import com.ua.polishchuk.entity.Task;
import com.ua.polishchuk.entity.TaskStatus;
import com.ua.polishchuk.repository.TaskRepository;
import com.ua.polishchuk.service.mapper.EntityMapper;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;
import java.util.Optional;

public class TaskUtil {

    private static final String TASK_ALREADY_EXISTS = "Task with this title already exists: ";
    private static final String TASK_NOT_EXISTS = "Task not exists with provided id";
    private static final String NOT_UNIQUE_TITLE = "Not unique title exception";

    public static void checkIfNewTitleIsUniqueRelativeToOtherTasks(String title, Integer id, TaskRepository taskRepository) {
        Optional<Task> task = taskRepository.findByTitle(title);

        if(task.isPresent() && !task.get().getId().equals(id)){
            throw new NonUniqueResultException(NOT_UNIQUE_TITLE);
        }
    }

    public static Task getTaskIfExists(Integer taskId, TaskRepository taskRepository) {
        Optional<Task> task = taskRepository.findById(taskId);
        if(!task.isPresent()){
            throw new EntityNotFoundException(TASK_NOT_EXISTS);
        }
        return task.get();
    }

    public static Task setParametersForEditingTask(Task task, EditTaskDto editTaskDto) {
        return Task.builder()
                .id(task.getId())
                .user(task.getUser())
                .status(task.getStatus())
                .title(editTaskDto.getTitle())
                .description(editTaskDto.getDescription())
                .build();
    }

    public static Task prepareTaskForSaving(TaskDto taskDto, EntityMapper<Task, TaskDto> mapper){
        Task taskToSave = mapper.mapDtoToEntity(taskDto);

        if(taskToSave.getStatus() == null){
            taskToSave.setStatus(TaskStatus.VIEW);
        }
        return taskToSave;
    }

    public static void checkIfTaskAlreadyExists(TaskDto taskDto, TaskRepository taskRepository){
        taskRepository.findByTitle(taskDto.getTitle()).ifPresent(task -> {
            throw new EntityExistsException(TASK_ALREADY_EXISTS + taskDto.getTitle());
        });
    }
}
