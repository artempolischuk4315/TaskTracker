package com.ua.polishchuk.controller;

import com.ua.polishchuk.dto.TaskDto;
import com.ua.polishchuk.dto.TaskFieldsToEdit;
import com.ua.polishchuk.entity.TaskStatus;
import com.ua.polishchuk.facade.TaskFacade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import sun.security.acl.PrincipalImpl;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    private static final String WRONG_STATUS = "Wrong status";
    private static final int ID = 1;
    private static final String DESCRIPTION = "Description";
    private static final TaskStatus STATUS = TaskStatus.VIEW;
    private static final String TITLE = "Title";
    private static final int USER_ID = 2;

    private static final TaskDto taskDto = getTaskDto();
    private static final TaskFieldsToEdit fieldsToEdit = getTaskFieldsToEdit();
    private static final String WRONG_ORDER = "wrong-order";
    private static final String ORDER = "asc";

    @InjectMocks
    TaskController systemUnderTest;

    @Mock
    TaskFacade taskFacade;

    @Mock
    BindingResult bindingResult;

    @Test
    void readAllSortedByUserShouldReturnBadRequestStatusIfOrderTypeWrong(){
        ResponseEntity actual = systemUnderTest.readAllSortedByUser(WRONG_ORDER);
        ResponseEntity expected = ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        assertEquals(expected, actual);
    }

    @Test
    void readAllSortedByUserShouldReturnOkStatusWithListIfOrderTypeCorrect(){
        ResponseEntity actual = systemUnderTest.readAllSortedByUser(ORDER);
        ResponseEntity<List<TaskDto>> expected = ResponseEntity.status(HttpStatus.OK).body(Collections.emptyList());

        assertEquals(expected, actual);
    }

    @Test
    void readAllFilteredByStatusShouldReturnBadRequestIfPassedStatusWrong(){
        ResponseEntity actual = systemUnderTest.readAllFilteredByStatus(WRONG_STATUS);
        ResponseEntity expected = ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        assertEquals(expected, actual);
    }

    @Test
    void readAllFilteredByStatusShouldReturnOkIfPassedStatusCorrect(){
        ResponseEntity actual = systemUnderTest.readAllFilteredByStatus(STATUS.toString());
        ResponseEntity<List<TaskDto>> expected = ResponseEntity.status(HttpStatus.OK).body(Collections.emptyList());

        assertEquals(expected, actual);
    }

    @Test
    void createShouldReturnBadRequestStatusIfBindingHasErrors(){
        when(bindingResult.hasErrors()).thenReturn(true);

        Principal principal = new PrincipalImpl("principal");

        ResponseEntity<Object> response = systemUnderTest.create(taskDto, bindingResult, principal);

        HttpStatus actual = response.getStatusCode();
        HttpStatus expected = HttpStatus.BAD_REQUEST;

        assertEquals(expected, actual);
    }

    @Test
    void createShouldReturnOkWithSavedTaskIfRequestCorrect(){
        Principal principal = new PrincipalImpl("principal");
        when(bindingResult.hasErrors()).thenReturn(false);
        when(taskFacade.save(taskDto, principal)).thenReturn(taskDto);

        ResponseEntity<Object> actual = systemUnderTest.create(taskDto, bindingResult, principal);
        ResponseEntity<Object> expected = ResponseEntity.status(HttpStatus.OK).body(taskDto);

        assertEquals(expected, actual);
    }

    @Test
    void updateShouldReturnBadRequestStatusIfBindingHasErrors(){
        when(bindingResult.hasErrors()).thenReturn(true);

        ResponseEntity<Object> response = systemUnderTest.update(ID, fieldsToEdit, bindingResult);

        HttpStatus actual = response.getStatusCode();
        HttpStatus expected = HttpStatus.BAD_REQUEST;

        assertEquals(expected, actual);
    }

    @Test
    void updateShouldReturnOkStatusWithTaskIfRequestCorrect(){
        when(bindingResult.hasErrors()).thenReturn(false);
        when(taskFacade.edit(ID, fieldsToEdit)).thenReturn(taskDto);

        ResponseEntity<Object> actual = systemUnderTest.update(ID, fieldsToEdit, bindingResult);
        ResponseEntity<Object> expected = ResponseEntity.status(HttpStatus.OK).body(taskDto);

        assertEquals(expected, actual);
    }

    @Test
    void changeStatusShouldReturnBadRequestStatusIfPassedStatusInvalid(){
        ResponseEntity<Object> actual = systemUnderTest.changeStatus(ID, WRONG_STATUS);
        ResponseEntity<Object> expected = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(WRONG_STATUS);

        assertEquals(expected, actual);
    }

    @Test
    void changeStatusShouldReturnOkStatusIfPassedStatusValid(){
        when(taskFacade.changeStatus(ID, STATUS)).thenReturn(taskDto);

        ResponseEntity<Object> actual = systemUnderTest.changeStatus(ID, STATUS.toString());
        ResponseEntity<Object> expected = ResponseEntity.status(HttpStatus.OK).body(taskDto);

        assertEquals(expected, actual);
    }

    @Test
    void changeUserShouldReturnOkStatusWithUserDto(){
        when(taskFacade.changeUser(ID, USER_ID)).thenReturn(taskDto);

        ResponseEntity<Object> actual = systemUnderTest.changeUser(ID, USER_ID);
        ResponseEntity<Object> expected = ResponseEntity.status(HttpStatus.OK).body(taskDto);

        assertEquals(expected, actual);
    }

    @Test
    void deleteShouldReturnOkStatus(){
        ResponseEntity<Void> actual = systemUnderTest.delete(ID);
        ResponseEntity<Void> expected = ResponseEntity.status(HttpStatus.OK).build();

        verify(taskFacade).delete(ID);
        assertEquals(expected, actual);
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
