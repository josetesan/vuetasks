package com.josetesan.oracle.rest.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.josetesan.oracle.rest.api.Task;
import com.josetesan.oracle.rest.db.TaskDao;
import io.dropwizard.jersey.validation.ValidationErrorMessage;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link TaskResource}.
 */
@ExtendWith(DropwizardExtensionsSupport.class)
class TaskResourceTest {

    private final ArgumentCaptor<Task> argumentCaptor = ArgumentCaptor.forClass(Task.class);

    private static final TaskDao taskDao = mock(TaskDao.class);
    private static final ResourceExtension RESOURCES = ResourceExtension.builder()
            .addResource(new TaskResource(taskDao))
            .build();

    @AfterEach
    public void clean(){
        reset(taskDao);
    }

    @Test
    void getAllTasks() throws Exception {

        Task task = new Task(1L,"task","2020-01-20");
        when(taskDao.findAll()).thenReturn(List.of(task));

        Response response  = RESOURCES
                .target("/tasks")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        ObjectMapper objectMapper = new ObjectMapper();
        String stringResponse = response.readEntity(String.class);
        List<Task> tasks = objectMapper.readerForListOf(Task.class).readValue(stringResponse);

        assertThat(tasks.size()).isEqualTo(1);
        assertThat(tasks.get(0)).isEqualTo(task);

        verify(taskDao,times(1)).findAll();

    }

    @Test
    void getNoTasks() throws Exception {

        when(taskDao.findAll()).thenReturn(Collections.emptyList());

        Response response  = RESOURCES
                .target("/tasks")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        ObjectMapper objectMapper = new ObjectMapper();
        String stringResponse = response.readEntity(String.class);
        List<Task> tasks = objectMapper.readerForListOf(Task.class).readValue(stringResponse);

        assertThat(tasks).isEmpty();
        verify(taskDao,times(1)).findAll();
    }



    @Test
    void createTask() {
        Task task = new Task(1L,"task","2020-01-20");
        when(taskDao.insert(any(Task.class))).thenReturn(1L);
        final Response response = RESOURCES.target("/tasks")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(task, MediaType.APPLICATION_JSON_TYPE));

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
        verify(taskDao).insert(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue()).isEqualTo(task);
    }

    @Test
    void createEmptyTask() {
        Task task = new Task(null,null,null);
        final Response response = RESOURCES.target("/tasks")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(task, MediaType.APPLICATION_JSON_TYPE));

        assertThat(response).isNotNull();

        assertThat(response.getStatusInfo().getFamily()).isEqualTo(Response.Status.Family.CLIENT_ERROR);
        assertThat(response.getStatus()).isEqualTo(422);
        ValidationErrorMessage msg = response.readEntity(ValidationErrorMessage.class);
        assertThat(msg.getErrors())
                .containsExactlyInAnyOrder(
                        "dueDate must not be null",
                        "dueDate must not be empty"
                        ,"name must not be empty"
                        ,"name must not be null");
        verify(taskDao,times(0)).insert(any(Task.class));
    }


}
