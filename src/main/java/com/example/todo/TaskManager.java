package com.example.todo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TaskManager {
    private final List<Task> tasks;
    private final TaskStorage storage;
    private int nextId;

    public TaskManager(TaskStorage storage) throws IOException {
        this.storage = storage;
        this.tasks = new ArrayList<>(storage.load());
        this.nextId = tasks.stream()
                .mapToInt(Task::getId)
                .max()
                .orElse(0) + 1;
    }

    public void addTask(String title) throws IOException {
        Task task = new Task(nextId, title, false);
        tasks.add(task);
        storage.save(tasks);
        nextId++;
    }

    public void completeTask(int id) throws IOException {
        Task task = tasks.stream()
                .filter(item -> item.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Task not found."));
        task.markCompleted();
        storage.save(tasks);
    }

    public List<Task> getAllTasks() {
        return List.copyOf(tasks);
    }

    public List<Task> getOpenTasks() {
        return tasks.stream()
                .filter(task -> !task.isCompleted())
                .toList();
    }
}
