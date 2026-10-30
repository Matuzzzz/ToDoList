package com.example.todo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CsvTaskStorage implements TaskStorage {
    private final Path file;

    public CsvTaskStorage(Path file) {
        this.file = file;
    }

    @Override
    public Map<Integer, Task> load() throws IOException {
        Map<Integer, Task> tasks = new HashMap<>();

        if (!Files.exists(file)) {
            return tasks;
        }

        List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);

        for (int index = 0; index < lines.size(); index++) {
            String[] parts = lines.get(index).split(";", 3);

            if (parts.length != 3) {
                throw new IOException("Invalid CSV format on line " + (index + 1) + ".");
            }

            try {
                int id = Integer.parseInt(parts[0]);
                boolean completed = parseCompleted(parts[1]);
                Task task = new Task(id, parts[2], completed);

                if (tasks.putIfAbsent(id, task) != null) {
                    throw new IllegalArgumentException("Duplicate task ID: " + id);
                }
            } catch (IllegalArgumentException exception) {
                throw new IOException(
                        "Invalid task data on line " + (index + 1) + ": " + exception.getMessage(),
                        exception
                );
            }
        }

        return tasks;
    }

    @Override
    public void save(Map<Integer, Task> tasks) throws IOException {
        List<String> lines = tasks.values().stream()
                .sorted((first, second) -> Integer.compare(first.getId(), second.getId()))
                .map(task -> task.getId() + ";" + task.isCompleted() + ";" + task.getTitle())
                .toList();

        Files.write(file, lines, StandardCharsets.UTF_8);
    }

    private boolean parseCompleted(String value) {
        if (value.equalsIgnoreCase("true")) {
            return true;
        }
        if (value.equalsIgnoreCase("false")) {
            return false;
        }
        throw new IllegalArgumentException("Task status must be true or false.");
    }
}
