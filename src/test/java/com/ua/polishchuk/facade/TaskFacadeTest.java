package com.ua.polishchuk.facade;

import com.ua.polishchuk.dto.TaskDto;
import com.ua.polishchuk.dto.TaskFieldsToEdit;
import com.ua.polishchuk.entity.Task;
import com.ua.polishchuk.entity.TaskStatus;
import com.ua.polishchuk.entity.User;
import com.ua.polishchuk.service.TaskService;
import com.ua.polishchuk.service.mapper.EntityMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sun.security.acl.PrincipalImpl;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskFacadeTest {

    private static final int ID = 1;
    private static final String DESCRIPTION = "Description";
    private static final TaskStatus STATUS = TaskStatus.VIEW;
    private static final String TITLE = "Title";
    private static final int USER_ID = 2;
    private static final String ORDER = "asc";

    private static final Task task = getTask();
    private static final TaskDto taskDto = getTaskDto();
    private static final TaskFieldsToEdit fieldsToEdit = getTaskFieldsToEdit();

    @InjectMocks
    TaskFacade systemUnderTest;

    @Mock
    TaskService taskService;

    @Mock
    EntityMapper<Task, TaskDto> mapper;

    @Test
    void findSortedByUserShouldReturnListOfUsersDto(){
        when(taskService.findSortedByUserFromOldToNew()).thenReturn(Collections.singletonList(task));
        when(mapper.mapEntityToDto(task)).thenReturn(taskDto);

        List<TaskDto> actual = systemUnderTest.findSortedByUser(ORDER);
        List<TaskDto> expected = Collections.singletonList(taskDto);

        assertEquals(expected, actual);
    }

    @Test
    void findFilteredByStatusShouldReturnListOfUsersDto(){
        when(taskService.findByStatus(STATUS)).thenReturn(Collections.singletonList(task));
        when(mapper.mapEntityToDto(task)).thenReturn(taskDto);

        List<TaskDto> actual = systemUnderTest.findFilteredByStatus(STATUS);
        List<TaskDto> expected = Collections.singletonList(taskDto);

        assertEquals(expected, actual);
    }

    @Test
    void saveShouldReturnTaskDto(){
        Principal principal = new PrincipalImpl("principal");

        when(taskService.save(task, principal)).thenReturn(task);
        when(mapper.mapEntityToDto(task)).thenReturn(taskDto);
        when(mapper.mapDtoToEntity(taskDto)).thenReturn(task);

        TaskDto actual = systemUnderTest.save(taskDto, principal);

        assertEquals(taskDto, actual);
    }

    @Test
    void editShouldReturnTaskDto(){
        when(mapper.mapEntityToDto(task)).thenReturn(taskDto);
        when(taskService.edit(ID, fieldsToEdit)).thenReturn(task);

        TaskDto actual = systemUnderTest.edit(ID, fieldsToEdit);

        assertEquals(taskDto, actual);
    }

    @Test
    void changeStatusShouldReturnTaskDto(){
        when(mapper.mapEntityToDto(task)).thenReturn(taskDto);
        when(taskService.changeStatus(ID, STATUS)).thenReturn(task);

        TaskDto actual = systemUnderTest.changeStatus(ID, STATUS);

        assertEquals(taskDto, actual);
    }

    @Test
    void changeUserShouldReturnTaskDto(){
        when(mapper.mapEntityToDto(task)).thenReturn(taskDto);
        when(taskService.changeUser(ID, USER_ID)).thenReturn(task);

        TaskDto actual = systemUnderTest.changeUser(ID, USER_ID);

        assertEquals(taskDto, actual);
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

    private static TaskFieldsToEdit getTaskFieldsToEdit(){
        return TaskFieldsToEdit.builder()
                .description(DESCRIPTION)
                .title(TITLE)
                .build();
    }
}
