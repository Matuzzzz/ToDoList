package com.example.todo;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ConsoleInterface {
    private final CommandRegistry commandRegistry = new CommandRegistry();
    private TaskManager taskManager;
    private Scanner scanner;
    private boolean running;

    public ConsoleInterface() {
        registerDefaultCommands();
    }

    public void start() {
        try (Scanner input = new Scanner(System.in)) {
            scanner = input;

            try {
                taskManager = new TaskManager(new CsvTaskStorage(Path.of("tasks.csv")));
            } catch (IOException exception) {
                printError("Could not load tasks.csv: " + exception.getMessage());
                return;
            }

            running = true;
            while (running && scanner.hasNextLine()) {
                printMenu();
                executeCommand(scanner.nextLine());
            }
        } finally {
            scanner = null;
        }
    }

    //Register Command a úpraava Key
    public void registerCommand(String key, String description, CommandRegistry.CommandAction action) {
        commandRegistry.register(key, description, action);
    }

    /** Changes a command's shortcut without changing its action or description. */
    public void changeCommandShortcut(String currentKey, String newKey) {
        commandRegistry.changeShortcut(currentKey, newKey);
    }

    private void registerDefaultCommands() {
        // Change a shortcut here once; the menu and command lookup update automatically.
        registerCommand("1", "Add task", this::addTask);
        registerCommand("2", "Show all tasks", () -> printTasks(taskManager.getAllTasks()));
        registerCommand("3", "Show open tasks", () -> printTasks(taskManager.getOpenTasks()));
        registerCommand("4", "Mark task as done", this::completeTask);
        registerCommand("5", "Delete task", this::deleteTask);
        registerCommand("6", "Change a command shortcut", this::changeCommandShortcut);
        registerCommand("0", "Exit", this::exit);
    }

    private void executeCommand(String key) {
        try {
            if (!commandRegistry.execute(key)) {
                printError("Unknown command. Choose one of the keys shown in the menu.");
            }
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
        int id = readTaskId();
        taskManager.completeTask(id);
        printMessage("Task marked as done.");
    }

    private void deleteTask() throws IOException {
        int id = readTaskId();
        taskManager.deleteTask(id);
        printMessage("Task deleted.");
    }

    private int readTaskId() {
        printPrompt("Task ID: ");
        String input = scanner.nextLine().trim();

        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Task ID must be a whole number.");
        }
    }

    private void changeCommandShortcut() {
        printPrompt("Current command key: ");
        String currentKey = scanner.nextLine();
        printPrompt("New command key: ");
        String newKey = scanner.nextLine();

        changeCommandShortcut(currentKey, newKey);
        printMessage("Command shortcut updated.");
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
        commandRegistry.getCommands().entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> printMessage(entry.getKey() + " - " + entry.getValue().description()));
        printPrompt("Choose: ");
    }

    public void printMessage(String message) {
        System.out.println(message);
    }

    private void printPrompt(String message) {
        System.out.print(message);
    }

    private void printError(String message) {
        System.err.println("Error: " + message);
    }
}
