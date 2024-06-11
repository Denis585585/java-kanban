package service;

import dao.Dao;
import dao.TaskDao;
import model.EpicTask;
import model.Task;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class TaskManager implements Manager<Task>{
    private final Dao<Task> dao;

    public TaskManager(TaskDao dao) {
        this.dao = dao;
    }

    @Override
    public void clearAllTasks() {
        dao.clearAllTasks();
    }

    @Override
    public Optional<Task> findById(int id) {
        return dao.findById(id);
    }

    @Override
    public void add(Task object) {
        //todo кастануть к эпику, проверить наличие и статус подзадач, и на основании этого присвоить статус эпику
        dao.add(object);
    }

    @Override
    public boolean update(int id, Task object) {
        return dao.update(id, object);
    }

    @Override
    public boolean remove(int id) {
        return dao.remove(id);
    }

    public List<Task> getSubtasks(int id){
        Optional<Task> taskOptional = dao.findById(id);
        if (taskOptional.isEmpty()) {
            String message = String.format("task with id = %s not found", id);
            throw new NoSuchElementException(message);
        } else {
            Task task = taskOptional.get();
            try {
                EpicTask epic = (EpicTask) task;
                return epic.getSubtasks();
            } catch (ClassCastException e){
                throw new RuntimeException("There are no subtasks in task with id = " + id);
            }
        }
    }
}
