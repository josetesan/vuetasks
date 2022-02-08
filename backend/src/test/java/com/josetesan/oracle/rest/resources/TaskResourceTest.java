package com.josetesan.oracle.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.josetesan.oracle.rest.api.Task;
import com.josetesan.oracle.rest.db.TaskDao;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import liquibase.pro.packaged.E;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.testcontainers.shaded.com.fasterxml.jackson.core.type.TypeReference;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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

        verify(taskDao).findAll();
    }


    @Test
    void createPerson() {
        Task task = new Task(1L,"task","2020-01-20");
        when(taskDao.insert(any(Task.class))).thenReturn(1L);
        final Response response = RESOURCES.target("/tasks")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(task, MediaType.APPLICATION_JSON_TYPE));

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
        verify(taskDao).insert(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue()).isEqualTo(task);
    }



}
