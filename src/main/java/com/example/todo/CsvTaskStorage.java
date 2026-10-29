package com.example.todo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CsvTaskStorage implements TaskStorage {
    private final Path file;

    public CsvTaskStorage(Path file) {
        this.file = file;
    }

    @Override
    public List<Task> load() throws IOException {
        List<Task> tasks = new ArrayList<>();
        if (!Files.exists(file)) {
            return tasks;
        }

        for (String line : Files.readAllLines(file)) {
            String[] parts = line.split(";", 3);
            if (parts.length != 3) {
                continue;
            }
            try {
                int id = Integer.parseInt(parts[0]);
                boolean completed = Boolean.parseBoolean(parts[1]);
                tasks.add(new Task(id, parts[2], completed));
            } catch (IllegalArgumentException e) {
                System.out.println("Skipping an invalid row in tasks.csv.");
            }
        }
        return tasks;
    }

    @Override
    public void save(List<Task> tasks) throws IOException {
        List<String> lines = new ArrayList<>();
        for (Task task : tasks) {
            lines.add(task.getId() + ";" + task.isCompleted() + ";" + task.getTitle());
        }
        Files.write(file, lines);
    }
}
