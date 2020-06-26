package com.ua.polishchuk.facade;

import com.ua.polishchuk.entity.Task;
import com.ua.polishchuk.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TaskFacade {

    private static final String ASC = "asc";

    private final TaskService taskService;

    @Autowired
    public TaskFacade(TaskService taskService) {
        this.taskService = taskService;
    }

    public List<Task> findSortedByUser(String order){
        if(order.equals(ASC)){
            return taskService.findSortedByUserFromOldToNew();
        }

        return taskService.findSortedByUserFromNewToOld();
    }
}
