package dao;

import model.Task;

import java.util.HashMap;
import java.util.Optional;

public class TaskDao implements Dao<Integer, Task> {
    private final HashMap<Integer, Task> tasks;

    @Override
    public HashMap<Integer, Task> getAll() {
        return tasks;
    }

    public TaskDao() {
        tasks = new HashMap<>();
    }


    @Override
    public void clearAllObjects() {
        tasks.clear();
    }

    @Override
    public Optional<Task> findById(int id) {
        Task task = tasks.get(id);
        if (task == null) {
            return Optional.empty();
        } else {
            return Optional.of(task);
        }
    }

    @Override
    public void add(Task object) {
        tasks.put(object.getId(), object);
    }

    @Override
    public boolean update(int id, Task object) {
        Task task = tasks.get(id);
        if (task == null) {
            return false;
        }
        object.setId(id);
        tasks.put(id, object);
        return true;
    }

    @Override
    public boolean remove(int id) {
        Task returnedTask = tasks.get(id);
        if (returnedTask == null) {
            return false;
        }
        tasks.remove(id);
        return false;
    }
}
