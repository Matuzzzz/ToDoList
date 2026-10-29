package com.example.todo;

import java.io.IOException;
import java.util.List;

public interface TaskStorage {
    List<Task> load() throws IOException;
    void save(List<Task> tasks) throws IOException;
}
