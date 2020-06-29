package com.ua.polishchuk.service.mapper.impl;

import com.ua.polishchuk.dto.TaskDto;
import com.ua.polishchuk.entity.Task;
import com.ua.polishchuk.entity.User;
import com.ua.polishchuk.service.mapper.EntityMapper;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper implements EntityMapper<Task, TaskDto> {

    @Override
    public TaskDto mapEntityToDto(Task entity) {
        return TaskDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .status(entity.getStatus())
                .description(entity.getDescription())
                .userId(entity.getUser().getId())
                .build();
    }

    @Override
    public Task mapDtoToEntity(TaskDto dto) {
        return Task.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .status(dto.getStatus())
                .description(dto.getDescription())
                .user(User.builder().id(dto.getUserId()).build())
                .build();
    }
}
