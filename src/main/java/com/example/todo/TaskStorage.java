package com.example.todo;

import java.io.IOException;
import java.util.Map;

public interface TaskStorage {
    Map<Integer, Task> load() throws IOException;

    void save(Map<Integer, Task> tasks) throws IOException;
}
