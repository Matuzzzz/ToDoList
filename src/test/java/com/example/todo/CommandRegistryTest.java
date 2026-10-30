package com.example.todo;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommandRegistryTest {
    @Test
    void registersAndExecutesCommand() throws Exception {
        CommandRegistry registry = new CommandRegistry();
        AtomicBoolean executed = new AtomicBoolean(false);
        registry.register("a", "Test command", () -> executed.set(true));

        assertTrue(registry.execute(" A "));
        assertTrue(executed.get());
    }

    @Test
    void changesShortcutWithoutChangingCommand() throws Exception {
        CommandRegistry registry = new CommandRegistry();
        AtomicBoolean executed = new AtomicBoolean(false);
        registry.register("A", "Test command", () -> executed.set(true));

        registry.changeShortcut("A", "B");

        assertFalse(registry.execute("A"));
        assertTrue(registry.execute("B"));
        assertTrue(executed.get());
    }

    @Test
    void rejectsShortcutThatIsAlreadyInUse() {
        CommandRegistry registry = new CommandRegistry();
        registry.register("A", "First command", () -> { });
        registry.register("B", "Second command", () -> { });

        assertThrows(IllegalArgumentException.class, () -> registry.changeShortcut("A", "B"));
    }

    @Test
    void canRegisterAndRemoveCommandsDynamically() {
        CommandRegistry registry = new CommandRegistry();
        registry.register("H", "Help", () -> { });

        assertTrue(registry.getCommands().containsKey("H"));
        assertTrue(registry.unregister("H"));
        assertFalse(registry.getCommands().containsKey("H"));
    }
}
