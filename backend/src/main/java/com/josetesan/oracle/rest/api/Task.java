package com.josetesan.oracle.rest.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jdbi.v3.core.mapper.reflect.ColumnName;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;


public class Task {

    private Long id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String dueDate;

    /** needed for inserts **/
    public Task() {
    }



    /** needed for get **/
    public Task(@ColumnName("id") Long id, @ColumnName("name") String name, @ColumnName("dueDate") String dueDate) {
        this.id = id;
        this.name = name;
        this.dueDate = dueDate;
    }

    @JsonProperty
    public Long getId() {
        return id;
    }
    @NotNull
    @JsonProperty
    public String getName() {
        return name;
    }
    @NotNull
    @JsonProperty
    public String getDueDate() {
        return dueDate;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(getId(), task.getId()) && Objects.equals(getName(), task.getName()) && Objects.equals(getDueDate(), task.getDueDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDueDate());
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
}
