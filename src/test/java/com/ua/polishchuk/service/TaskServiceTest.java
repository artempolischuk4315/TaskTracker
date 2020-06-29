package com.ua.polishchuk.service;

import com.ua.polishchuk.dto.TaskFieldsToEdit;
import com.ua.polishchuk.entity.Task;
import com.ua.polishchuk.entity.TaskStatus;
import com.ua.polishchuk.entity.User;
import com.ua.polishchuk.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    private static final int ID = 1;
    private static final String DESCRIPTION = "Description";
    private static final TaskStatus STATUS = TaskStatus.VIEW;
    private static final String TITLE = "Title";
    private static final int USER_ID = 2;
    private static final Integer ANOTHER_ID = 120;
    private static final String ANOTHER_TITLE = "Another title";

    private static Task task;
    private static final TaskFieldsToEdit fieldsToEdit = getTaskFieldsToEdit();
    private static final List<Task> taskList = getListOfTasks();

    @InjectMocks
    TaskService systemUnderTest;

    @Mock
    TaskRepository taskRepository;

    @BeforeEach
    void setUp(){
        task = getTask();
    }

    @Test
    void saveShouldThrowEntityExistsExceptionIfTaskAlreadyExists(){
        when(taskRepository.findByTitle(TITLE)).thenReturn(Optional.of(task));

        assertThrows(EntityExistsException.class, () -> systemUnderTest.save(task, User.builder().id(USER_ID).build()));
    }

    @Test
    void saveShouldReturnSavedTask(){
        when(taskRepository.findByTitle(TITLE)).thenReturn(Optional.empty());
        when(taskRepository.save(task)).thenReturn(task);

        Task actual = systemUnderTest.save(task, User.builder().id(USER_ID).build());
        assertEquals(task, actual);
    }

    @Test
    void editShouldThrowEntityNotFoundExceptionIfTaskNotExists(){
        when(taskRepository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> systemUnderTest.edit(ID, fieldsToEdit));
    }

    @Test
    void editShouldThrowNonUniqueResultExceptionIfTitleNotUnique(){

        when(taskRepository.findById(ID)).thenReturn(Optional.of(task));
        when(taskRepository.findByTitle(ANOTHER_TITLE)).thenReturn(Optional.of(Task.builder().id(ANOTHER_ID).build()));

        assertThrows(NonUniqueResultException.class, () -> systemUnderTest.edit(ID, fieldsToEdit));
    }

    @Test
    void editShouldReturnEditedTask(){

        when(taskRepository.findById(ID)).thenReturn(Optional.of(task));
        when(taskRepository.findByTitle(fieldsToEdit.getTitle())).thenReturn(Optional.empty());
        when(taskRepository.save(task)).thenReturn(task);

        assertEquals(TITLE, task.getTitle());

        Task actual = systemUnderTest.edit(ID, fieldsToEdit);

        assertEquals(ANOTHER_TITLE, task.getTitle());
        assertEquals(task, actual);
    }

    @Test
    void changeStatusShouldReturnTaskWithChangedStatus(){
        when(taskRepository.findById(ID)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);

        assertEquals(TaskStatus.VIEW, task.getStatus());

        Task actual = systemUnderTest.changeStatus(ID, TaskStatus.DONE);

        assertEquals(TaskStatus.DONE, task.getStatus());
        assertEquals(task, actual);
    }

    @Test
    void changeUserShouldReturnTaskWithChangedUser(){
        when(taskRepository.findById(ID)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);

        Integer id = task.getUser().getId();
        assertSame(USER_ID, id);

        Task actual = systemUnderTest.changeUser(ID, User.builder().id(ANOTHER_ID).build());

        id = task.getUser().getId();
        assertSame(ANOTHER_ID, id);
        assertEquals(task, actual);
    }

    @Test
    void findByStatusShouldReturnListOfTasksWithTheSameStatus(){
        when(taskRepository.findAll()).thenReturn(taskList);

        List<Task> actual = systemUnderTest.findByStatus(TaskStatus.VIEW);

        assertEquals(TaskStatus.VIEW, actual.get(0).getStatus());
        assertEquals(TaskStatus.VIEW, actual.get(1).getStatus());
        assertEquals(2, actual.size());
    }

    @Test
    void findSortedByUserFromOldToNewShouldReturnListOfTasksSortedByUserIdFromOldToNew(){
        when(taskRepository.findAll()).thenReturn(taskList);

        List<Task> actual = systemUnderTest.findSortedByUserFromOldToNew();

        assertSame(0, actual.get(0).getUser().getId());
        assertSame(1, actual.get(1).getUser().getId());
        assertSame(2, actual.get(2).getUser().getId());
        assertSame(3, actual.get(3).getUser().getId());
    }

    @Test
    void findSortedByUserFromNewToOldShouldReturnListOfTasksSortedByUserIdFromNewToOld(){
        when(taskRepository.findAll()).thenReturn(taskList);

        List<Task> actual = systemUnderTest.findSortedByUserFromNewToOld();

        assertSame(3, actual.get(0).getUser().getId());
        assertSame(2, actual.get(1).getUser().getId());
        assertSame(1, actual.get(2).getUser().getId());
        assertSame(0, actual.get(3).getUser().getId());
    }

    @Test
    void deleteShouldInvokeRepository(){
        when(taskRepository.findById(ID)).thenReturn(Optional.of(task));

        systemUnderTest.delete(ID);

        verify(taskRepository).delete(task);
    }

    private static List<Task> getListOfTasks(){
        List<Task> taskList = new ArrayList<>();

        taskList.add(0, Task.builder().id(0).status(TaskStatus.VIEW).user(User.builder().id(0).build()).build());
        taskList.add(1, Task.builder().id(1).status(TaskStatus.VIEW).user(User.builder().id(2).build()).build());
        taskList.add(2, Task.builder().id(2).status(TaskStatus.IN_PROGRESS).user(User.builder().id(3).build()).build());
        taskList.add(3, Task.builder().id(3).status(TaskStatus.DONE).user(User.builder().id(1).build()).build());

        return taskList;
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

    private static TaskFieldsToEdit getTaskFieldsToEdit(){
        return TaskFieldsToEdit.builder()
                .description(DESCRIPTION)
                .title(ANOTHER_TITLE)
                .build();
    }
}
