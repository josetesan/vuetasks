package com.josetesan.oracle.rest.db;

import com.josetesan.oracle.rest.api.Task;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;
import java.util.Optional;

public interface TaskDao {

    @SqlQuery("select * from tasks")
    @RegisterBeanMapper(Task.class)
    List<Task> findAll();

    @SqlUpdate("delete from tasks where id = :id")
    void deleteById(@Bind("id") long id);

    @SqlUpdate("insert into tasks(name, dueDate) values (:name,:dueDate)")
    @GetGeneratedKeys("id")
    Long insert(@BindBean Task task);

    @SqlQuery("select * from tasks where id = :id")
    @RegisterBeanMapper(Task.class)
    Optional<Task> findById(@Bind("id") long id);

}
