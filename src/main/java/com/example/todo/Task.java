package com.example.todo;

public class Task {
    private final int id;
    private final String title;
    private boolean completed;

    public Task(int id, String title, boolean completed) {
        if (id < 1) {
            throw new IllegalArgumentException("ID must be positive.");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be empty.");
        }
        if (title.contains(";") || title.contains("\n") || title.contains("\r")) {
            throw new IllegalArgumentException("Title cannot contain semicolons or line breaks.");
        }
        this.id = id;
        this.title = title.trim();
        this.completed = completed;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void markCompleted() {
        completed = true;
    }
}
