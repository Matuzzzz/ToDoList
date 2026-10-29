package com.example.todo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskManagerTest {
    @TempDir
    Path tempDir;

    @Test
    void addsAndSavesTask() throws IOException {
        Path file = tempDir.resolve("tasks.csv");
        TaskManager manager = new TaskManager(new CsvTaskStorage(file));

        manager.addTask("Study Java");

        assertEquals(1, manager.getAllTasks().size());
        assertTrue(file.toFile().exists());
    }

    @Test
    void showsOnlyOpenTasks() throws IOException {
        TaskManager manager = new TaskManager(new CsvTaskStorage(tempDir.resolve("tasks.csv")));
        manager.addTask("First task");
        manager.addTask("Second task");
        manager.completeTask(1);

        List<Task> openTasks = manager.getOpenTasks();

        assertEquals(1, openTasks.size());
        assertEquals("Second task", openTasks.get(0).getTitle());
    }

    @Test
    void rejectsEmptyTitle() throws IOException {
        TaskManager manager = new TaskManager(new CsvTaskStorage(tempDir.resolve("tasks.csv")));

        assertThrows(IllegalArgumentException.class, () -> manager.addTask("   "));
    }
}
