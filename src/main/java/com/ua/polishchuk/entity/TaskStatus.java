package com.ua.polishchuk.entity;

public enum  TaskStatus {
    VIEW,
    IN_PROGRESS,
    DONE;

    public static boolean contains(String task){

        for(TaskStatus taskStatus : TaskStatus.values()){
            if(taskStatus.toString().equals(task)){
                return true;
            }
        }
        return false;
    }
}
