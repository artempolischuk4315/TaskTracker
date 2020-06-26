package com.ua.polishchuk.controller;

import com.ua.polishchuk.entity.Task;
import com.ua.polishchuk.facade.TaskFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskFacade taskFacade;

    @Autowired
    public TaskController(TaskFacade taskFacade) {
        this.taskFacade = taskFacade;
    }

    @GetMapping("/list/sort")
    public ResponseEntity<List<Task>> findAll(
                    @RequestParam(name = "order", defaultValue = "asc") String order){
        if(!isOrderParamValid(order)){
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(taskFacade.findSortedByUser(order));
    }

    private boolean isOrderParamValid(String order) {
        return order.equals("asc") || order.equals("desc");
    }
}
