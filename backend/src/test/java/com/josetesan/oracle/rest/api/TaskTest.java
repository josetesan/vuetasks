package com.josetesan.oracle.rest.api;

import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

class TaskTest {
    @Test
    void testUser() {
        Task task = new Task(1L,"task1", "2020-12-21");

        assertThat(task.getName()).isEqualTo("task1");
        assertThat(task.getDueDate()).isEqualTo("2020-12-21");
        assertThat(task.getId()).isNotNegative().isGreaterThan(0L);

    }
}
