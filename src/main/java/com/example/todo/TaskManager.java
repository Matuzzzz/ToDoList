package com.example.todo;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskManager {
    private final Map<Integer, Task> tasks;
    private final TaskStorage storage;
    private int nextId;

    public TaskManager(TaskStorage storage) throws IOException {
        this.storage = storage;
        this.tasks = new HashMap<>(storage.load());
        this.nextId = tasks.keySet().stream()
                .mapToInt(Integer::intValue)
                .max()
                .orElse(0) + 1;
    }

    public void addTask(String title) throws IOException {
        Task task = new Task(nextId, title, false);
        tasks.put(task.getId(), task);

        try {
            storage.save(tasks);
            nextId++;
        } catch (IOException exception) {
            tasks.remove(task.getId());
            throw exception;
        }
    }

    public void completeTask(int id) throws IOException {
        Task task = findTask(id);
        boolean previousStatus = task.isCompleted();
        task.setCompleted(true);

        try {
            storage.save(tasks);
        } catch (IOException exception) {
            task.setCompleted(previousStatus);
            throw exception;
        }
    }

    public void deleteTask(int id) throws IOException {
        Task removedTask = tasks.remove(id);
        if (removedTask == null) {
            throw new IllegalArgumentException("Task not found.");
        }

        try {
            storage.save(tasks);
        } catch (IOException exception) {
            tasks.put(id, removedTask);
            throw exception;
        }
    }

    public List<Task> getAllTasks() {
        return tasks.values().stream()
                .sorted(Comparator.comparingInt(Task::getId))
                .toList();
    }

    public List<Task> getOpenTasks() {
        return tasks.values().stream()
                .filter(task -> !task.isCompleted())
                .sorted(Comparator.comparingInt(Task::getId))
                .toList();
    }

    private Task findTask(int id) {
        Task task = tasks.get(id);
        if (task == null) {
            throw new IllegalArgumentException("Task not found.");
        }
        return task;
    }
}
