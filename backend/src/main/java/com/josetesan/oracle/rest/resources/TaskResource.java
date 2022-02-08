package com.josetesan.oracle.rest.resources;

import com.josetesan.oracle.rest.api.Task;
import com.josetesan.oracle.rest.db.TaskDao;
import com.josetesan.oracle.rest.service.TaskService;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/tasks")
@Produces(MediaType.APPLICATION_JSON)
public class TaskResource {

    private final TaskService taskService;

    public TaskResource(TaskDao taskDao) {
        this.taskService = new TaskService(taskDao);
    }

    @POST
    public Task create(@Valid final Task task) {
        return this.taskService.create(task);
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") final Long id){
        this.taskService.delete(id);

    }

    @GET
    public List<Task> listTasks() {
        return this.taskService.retrieveAll();
    }

}
