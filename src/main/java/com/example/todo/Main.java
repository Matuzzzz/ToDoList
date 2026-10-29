package com.example.todo;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            //Storage dát
            TaskManager manager = new TaskManager(new CsvTaskStorage(Path.of("tasks.csv")));
            boolean running = true;

            while (running) {
                System.out.println("\n=== TO-DO LIST ===");
                System.out.println("1 - Add task");
                System.out.println("2 - Show all tasks");
                System.out.println("3 - Show open tasks");
                System.out.println("4 - Mark task as done");
                System.out.println("0 - Exit");
                System.out.print("Choose: ");

                String choice = scanner.nextLine().trim();
                try {
                    switch (choice) {
                        case "1" -> {
                            System.out.print("Task title: ");
                            manager.addTask(scanner.nextLine());
                            System.out.println("Task saved.");
                        }
                        case "2" -> printTasks(manager.getAllTasks());
                        case "3" -> printTasks(manager.getOpenTasks());
                        case "4" -> {
                            System.out.print("Task ID: ");
                            int id = Integer.parseInt(scanner.nextLine().trim());
                            manager.completeTask(id);
                            System.out.println("Task marked as done.");
                        }
                        case "0" -> running = false;
                        default -> System.out.println("Please choose 0, 1, 2, 3, or 4.");
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Input error: " + e.getMessage());
                } catch (IOException e) {
                    System.out.println("File error: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Could not load tasks.csv: " + e.getMessage());
        }
    }

    private static void printTasks(List<Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println("No tasks found.");
            return;
        }
        for (Task task : tasks) {
            String status = task.isCompleted() ? "DONE" : "OPEN";
            System.out.println(task.getId() + " | " + task.getTitle() + " | " + status);
        }
    }
}
