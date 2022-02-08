package com.josetesan.oracle.rest.service;

import com.josetesan.oracle.rest.api.Task;
import com.josetesan.oracle.rest.db.TaskDao;

import java.util.List;

public  class TaskService {

    private TaskDao taskDao;

    public TaskService(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    public List<Task> retrieveAll() {
        return taskDao.findAll();
    }

    public void delete(long id) {
        taskDao.findById(id)
            .ifPresent(t -> this.taskDao.deleteById(t.getId()));
    }

    public Task create(Task task) {
         long id = this.taskDao.insert(task);
         return new Task(id, task.getName(),task.getDueDate());
    }
}
