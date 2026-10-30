package com.example.todo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        assertEquals("Study Java", new CsvTaskStorage(file).load().get(1).getTitle());
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

    @Test
    void returnsTasksInIdOrder() throws IOException {
        TaskManager manager = new TaskManager(new CsvTaskStorage(tempDir.resolve("tasks.csv")));
        manager.addTask("First task");
        manager.addTask("Second task");

        List<Integer> taskIds = manager.getAllTasks().stream()
                .map(Task::getId)
                .toList();

        assertEquals(List.of(1, 2), taskIds);
    }

    @Test
    void deletesTaskAndSavesChange() throws IOException {
        Path file = tempDir.resolve("tasks.csv");
        TaskManager manager = new TaskManager(new CsvTaskStorage(file));
        manager.addTask("First task");
        manager.addTask("Second task");

        manager.deleteTask(1);

        assertEquals(1, manager.getAllTasks().size());
        assertEquals("Second task", manager.getAllTasks().get(0).getTitle());
        assertTrue(new CsvTaskStorage(file).load().containsKey(2));
    }

    @Test
    void doesNotReuseTaskIdAfterDeletion() throws IOException {
        TaskManager manager = new TaskManager(new CsvTaskStorage(tempDir.resolve("tasks.csv")));
        manager.addTask("First task");
        manager.deleteTask(1);
        manager.addTask("Next task");

        assertEquals(2, manager.getAllTasks().get(0).getId());
    }

    @Test
    void rejectsDeletingUnknownTask() throws IOException {
        TaskManager manager = new TaskManager(new CsvTaskStorage(tempDir.resolve("tasks.csv")));

        assertThrows(IllegalArgumentException.class, () -> manager.deleteTask(99));
    }

    @Test
    void taskHasReadableStringRepresentation() {
        Task task = new Task(1, "Study Java", false);

        assertEquals("1 | Study Java | OPEN", task.toString());

        task.setCompleted(true);

        assertEquals("1 | Study Java | DONE", task.toString());
    }

    @Test
    void rejectsUnknownTaskId() throws IOException {
        TaskManager manager = new TaskManager(new CsvTaskStorage(tempDir.resolve("tasks.csv")));

        assertThrows(IllegalArgumentException.class, () -> manager.completeTask(99));
    }
}
