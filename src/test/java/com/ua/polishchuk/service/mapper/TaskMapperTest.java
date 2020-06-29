package com.ua.polishchuk.service.mapper;

import com.ua.polishchuk.dto.TaskDto;
import com.ua.polishchuk.entity.Task;
import com.ua.polishchuk.entity.TaskStatus;
import com.ua.polishchuk.entity.User;
import com.ua.polishchuk.service.mapper.impl.TaskMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class TaskMapperTest {

    private static final int ID = 1;
    private static final String DESCRIPTION = "Description";
    private static final TaskStatus STATUS = TaskStatus.VIEW;
    private static final String TITLE = "Title";
    private static final int USER_ID = 2;

    private static final Task task = getTask();
    private static final TaskDto taskDto = getTaskDto();

    private final EntityMapper<Task, TaskDto> mapper = new TaskMapper();

    @Test
    void mapEntityToDto_ShouldReturnDto() {
        TaskDto actual = mapper.mapEntityToDto(task);

        assertEquals(taskDto, actual);
    }

    @Test
    void mapDtoToEntity_ShouldReturnEntity() {
        Task actual = mapper.mapDtoToEntity(taskDto);

        assertEquals(task, actual);
    }


    private static Task getTask(){
        return Task.builder()
                .id(ID)
                .description(DESCRIPTION)
                .status(STATUS)
                .title(TITLE)
                .user(User.builder().id(USER_ID).build())
                .build();
    }

    private static TaskDto getTaskDto(){
        return TaskDto.builder()
                .id(ID)
                .description(DESCRIPTION)
                .status(STATUS)
                .title(TITLE)
                .userId(USER_ID)
                .build();
    }
}
