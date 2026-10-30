package com.example.todo;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

//Upravovuje shortcuty Commandov

public class CommandRegistry {
    @FunctionalInterface
    public interface CommandAction {
        void execute() throws IOException;
    }

    public record Command(String description, CommandAction action) {
    }

    private final Map<String, Command> commands = new HashMap<>();

    public void register(String key, String description, CommandAction action) {
        String normalizedKey = normalizeKey(key);

        //registerCommand(ContainsKey: "1", Description: "Add task", Action: this::addTask);

        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Command description cannot be empty.");
        }
        if (action == null) {
            throw new IllegalArgumentException("Command action cannot be null.");
        }
        if (commands.containsKey(normalizedKey)) {
            throw new IllegalArgumentException("A command already uses key: " + normalizedKey);
        }

        commands.put(normalizedKey, new Command(description.trim(), action));
    }

    public boolean unregister(String key) {
        return commands.remove(normalizeKey(key)) != null;
    }

    public void changeShortcut(String currentKey, String newKey) {
        String oldKey = normalizeKey(currentKey);
        String replacementKey = normalizeKey(newKey);

        Command command = commands.get(oldKey);
        if (command == null) {
            throw new IllegalArgumentException("No command uses key: " + oldKey);
        }
        if (!oldKey.equals(replacementKey) && commands.containsKey(replacementKey)) {
            throw new IllegalArgumentException("Key is already in use: " + replacementKey);
        }

        commands.remove(oldKey);
        commands.put(replacementKey, command);
    }

    public boolean execute(String key) throws IOException {
        Command command = commands.get(normalizeKey(key));
        if (command == null) {
            return false;
        }

        command.action().execute();
        return true;
    }

    /** Returns a read-only snapshot, so callers cannot change the registry accidentally. */
    public Map<String, Command> getCommands() {
        return Collections.unmodifiableMap(new HashMap<>(commands));
    }

    //Backhand logika Shrotcut/Keeys
    private String normalizeKey(String key) {
        if (key == null || key.isBlank()) {
            throw new IllegalArgumentException("Command key cannot be empty.");
        }

        String normalizedKey = key.trim().toUpperCase(Locale.ROOT);
        if (normalizedKey.chars().anyMatch(Character::isWhitespace)) {
            throw new IllegalArgumentException("Command key cannot contain spaces.");
        }
        return normalizedKey;
    }
}
