package com.josetesan.oracle.rest.service;

import com.josetesan.oracle.rest.api.Task;
import com.josetesan.oracle.rest.db.TaskDao;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class TaskServiceTest {

    private TaskDao taskDao;
    private TaskService taskService;

    @BeforeEach
    public void setup() {
        taskDao = mock(TaskDao.class);
        this.taskService = new TaskService(taskDao);
    }

    @AfterEach
    public void tearDown() {
        reset(taskDao);
    }

    @Test
    public void testRetrieveAll() {

        when(taskDao.findAll()).thenReturn(Collections.emptyList());
        Assertions.assertThat(taskService.retrieveAll()).isEmpty();

        Task task = mock(Task.class);
        when(taskDao.findAll()).thenReturn(List.of(task, task));
        Assertions.assertThat(taskService.retrieveAll()).hasSize(2);
        Assertions.assertThat(taskService.retrieveAll()).contains(task, task);

    }


    @Test
    public void testDeleteAnExistingItem() {

        Task task = mock(Task.class);
        when(task.getId()).thenReturn(1L);
        when(taskDao.findById(anyLong())).thenReturn(Optional.of(task));
        taskService.delete(1L);
        verify(taskDao, times(1)).deleteById(1L);
        verify(taskDao, times(1)).findById(1L);

    }

    @Test
    public void testDeleteANonExistingItem() {

        when(taskDao.findById(1)).thenReturn(Optional.empty());
        taskService.delete(1);
        verify(taskDao, times(1)).findById(1);
        verify(taskDao, times(0)).deleteById(1);
    }

    @Test
    public void testCreate() {

        Task task = new Task(1L,"task","2020-01-01");
        when(taskDao.insert(any(Task.class))).thenReturn(1L);
        Task anotherTask = taskService.create(task);
        verify(taskDao, times(1)).insert(any(Task.class));
        Assertions.assertThat(anotherTask).isEqualTo(task);

    }

}
