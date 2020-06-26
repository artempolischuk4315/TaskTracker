package com.ua.polishchuk.controller;

import com.ua.polishchuk.dto.TaskDto;
import com.ua.polishchuk.dto.TaskFieldsToEdit;
import com.ua.polishchuk.entity.TaskStatus;
import com.ua.polishchuk.facade.TaskFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private static final String WRONG_STATUS = "Wrong status";

    private final TaskFacade taskFacade;

    @Autowired
    public TaskController(TaskFacade taskFacade) {
        this.taskFacade = taskFacade;
    }

    @GetMapping("/list/sort")
    public ResponseEntity<List<TaskDto>> readAllSoertedByUser(
                    @RequestParam(name = "order", defaultValue = "asc") String order){
        if(!isOrderParamValid(order)){
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(taskFacade.findSortedByUser(order));
    }

    @GetMapping("/list/filter")
    public ResponseEntity<List<TaskDto>> readAllFilteredByStatus(
            @RequestParam(name = "status", defaultValue = "view") String status){
        if(!TaskStatus.contains(status.toUpperCase())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(taskFacade.findFilteredByStatus(TaskStatus.valueOf(status.toUpperCase())));
    }

    @PostMapping("")
    public ResponseEntity<Object> create(
            @Valid @RequestBody TaskDto taskDto, BindingResult bindingResult, Principal principal){

        if(bindingResult.hasErrors()){
            Map<String, Object> body = getAllErrorsList(bindingResult);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
        }

        return ResponseEntity.status(HttpStatus.OK).body(taskFacade.save(taskDto, principal));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Integer id,
                                         @Valid @RequestBody TaskFieldsToEdit editTaskDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            Map<String, Object> body = getAllErrorsList(bindingResult);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
        }
        return ResponseEntity.status(HttpStatus.OK).body(taskFacade.edit(id, editTaskDto));
    }

    @PutMapping("/{id}/status/change")
    public ResponseEntity<Object> changeStatus(@PathVariable Integer id,
                                               @RequestParam(name = "new-status", defaultValue = "view") String status){
        if(!TaskStatus.contains(status.toUpperCase())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(WRONG_STATUS);
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(taskFacade.changeStatus(id, TaskStatus.valueOf(status.toUpperCase())));
    }

    @PutMapping("/{task-id}/users/{user-id}")
    public ResponseEntity<Object> changeUser(
            @PathVariable(name = "task-id") Integer taskId, @PathVariable(name = "user-id") Integer userId){

        return ResponseEntity.status(HttpStatus.OK)
                .body(taskFacade.changeUser(taskId, userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id){
        taskFacade.delete(id);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    private boolean isOrderParamValid(String order) {
        return order.equals("asc") || order.equals("desc");
    }

    private Map<String, Object> getAllErrorsList(BindingResult bindingResult) {
        return bindingResult.getFieldErrors()
                .stream()
                .collect(Collectors.toMap(FieldError::getField,
                        DefaultMessageSourceResolvable::getDefaultMessage));
    }
}
