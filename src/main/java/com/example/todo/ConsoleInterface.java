package com.example.todo;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ConsoleInterface {
    private final Map<String, ConsoleCommand> commands = new HashMap<>();
    private TaskManager taskManager;
    private Scanner scanner;
    private boolean running;

    public void start() {
        try (Scanner input = new Scanner(System.in)) {
            scanner = input;

            try {
                taskManager = new TaskManager(new CsvTaskStorage(Path.of("tasks.csv")));
            } catch (IOException exception) {
                printError("Could not load tasks.csv: " + exception.getMessage());
                return;
            }

            registerCommands();
            running = true;

            while (running) {
                printMenu();
                executeCommand(scanner.nextLine().trim());
            }
        } finally {
            scanner = null;
        }
    }

    private void registerCommands() {
        commands.put("1", this::addTask);
        commands.put("2", () -> printTasks(taskManager.getAllTasks()));
        commands.put("3", () -> printTasks(taskManager.getOpenTasks()));
        commands.put("4", this::completeTask);
        commands.put("0", this::exit);
    }

    private void executeCommand(String choice) {
        ConsoleCommand command = commands.get(choice);

        if (command == null) {
            printError("Invalid option. Please choose 0, 1, 2, 3, or 4.");
            return;
        }

        try {
            command.execute();
        } catch (IllegalArgumentException exception) {
            printError("Input error: " + exception.getMessage());
        } catch (IOException exception) {
            printError("File error: " + exception.getMessage());
        }
    }

    private void addTask() throws IOException {
        printPrompt("Task title: ");
        taskManager.addTask(scanner.nextLine());
        printMessage("Task saved.");
    }

    private void completeTask() throws IOException {
        printPrompt("Task ID: ");
        String input = scanner.nextLine().trim();
        int id;

        try {
            id = Integer.parseInt(input);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Task ID must be a whole number.");
        }

        taskManager.completeTask(id);
        printMessage("Task marked as done.");
    }

    private void printTasks(List<Task> tasks) {
        if (tasks.isEmpty()) {
            printMessage("No tasks found.");
            return;
        }

        tasks.forEach(task -> printMessage(task.toString()));
    }

    private void exit() {
        running = false;
        printMessage("Goodbye.");
    }

    private void printMenu() {
        printMessage("\n=== TO-DO LIST ===");
        printMessage("1 - Add task");
        printMessage("2 - Show all tasks");
        printMessage("3 - Show open tasks");
        printMessage("4 - Mark task as done");
        printMessage("0 - Exit");
        printPrompt("Choose: ");
    }

    private void printMessage(String message) {
        System.out.println(message);
    }

    private void printPrompt(String message) {
        System.out.print(message);
    }

    private void printError(String message) {
        System.err.println("Error: " + message);
    }

    @FunctionalInterface
    private interface ConsoleCommand {
        void execute() throws IOException;
    }
}
