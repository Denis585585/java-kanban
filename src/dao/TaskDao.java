package dao;

import model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaskDao implements Dao<Task>{
    private final List<Task> tasks;

    public TaskDao() {
        tasks = new ArrayList<>(); //todo написать мапу и переделать под нее логику
    }


    @Override
    public void clearAllTasks() {
        tasks.clear();
    }

    @Override
    public Optional<Task> findById(int id) {
        for (Task task: tasks){
            if (task.getId() == id) {
                return Optional.of(task);
            }
        }
        return Optional.empty();
    }

    @Override
    public void add(Task object) {
        tasks.add(object);
    }

    @Override
    public boolean update(int id, Task object) {
        for (Task task: tasks){
            if (task.getId() == id) {
                updateObject(task, object);
                return true;
            }
        }
        return false;
    }

    private void updateObject(Task task, Task inputTask){
        task.setStatus(inputTask.getStatus());
        task.setTitle(inputTask.getTitle());
        task.setDescription(inputTask.getDescription());
    }

    @Override
    public boolean remove(int id) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId() == id) {
                tasks.remove(i);
                return true;
            }
        }
        return false;
    }
}
