# Simple To-Do List (Java)

A small console program for saving tasks.

## Features
- Add a task.
- Show all tasks.
- Show tasks that are not done.
- Mark a task as done.
- Save tasks to `tasks.csv`, so they are still there next time.

## Run
You need Java 17 and Maven.

Open this folder in IntelliJ IDEA, or run these commands in the project folder:

```bash
mvn test
mvn package
java -jar target/simple-todo-1.0.jar
```

## Main classes
- `Task` - one task; keeps its data private.
- `TaskManager` - adds, finds, and filters tasks.
- `TaskStorage` - an interface for saving and loading tasks.
- `CsvTaskStorage` - reads and writes `tasks.csv`.
- `Main` - shows the menu and reads user input.

## Required course topics
- **OOP:** classes, private fields, and an interface.
- **Collections:** `ArrayList` stores tasks in memory.
- **Streams and lambda:** `stream().filter(task -> !task.isCompleted())` returns open tasks.
- **File I/O:** `CsvTaskStorage` loads and saves the CSV file.
- **Exceptions:** invalid input and file errors are handled with `try-catch`.
- **Maven:** `pom.xml` builds the project and adds JUnit.
- **JUnit:** `TaskManagerTest` checks adding, filtering, and input validation.
- **Git:** make small commits as you add each feature.

The CSV format is `id;completed;title`. Do not use a semicolon in a task title.
