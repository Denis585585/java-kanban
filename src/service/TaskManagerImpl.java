package service;

import dao.Dao;
import model.EpicTask;
import model.Subtask;
import model.Task;
import util.TaskStatusEnum;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

public class TaskManagerImpl implements TaskManager<Integer, Task>, EpicTaskManager<Subtask> {
    private final Dao<Integer, Task> dao;

    public TaskManagerImpl(Dao<Integer, Task> dao) {
        this.dao = dao;
    }

    @Override
    public void clearAllObjects() {
        dao.clearAllObjects();
    }

    @Override
    public Optional<Task> findById(int id) {
        return dao.findById(id);
    }

    @Override
    public void add(Task object) {
        Optional<Task> taskOptional = dao.findById(object.getId());
        if (taskOptional.isPresent()) {
            throw new RuntimeException("Task with id = " + object.getId() + " is already exists");
        } else {
            setEpicStatusIfNotCorrect(object);
            dao.add(object);
        }
    }

    private void setEpicStatusIfNotCorrect(Task task) {
        if (task instanceof EpicTask epic) {
            Map<Integer, Subtask> subtasks = epic.getSubtasks();
            if (subtasks.isEmpty()) {
                epic.setStatus(TaskStatusEnum.NEW);
            }
            boolean isNew = true;
            boolean isDone = true;
            for (Task subtask : subtasks.values()) {
                if (!isNew && !isDone) break;
                if (!subtask.getStatus().equals(TaskStatusEnum.NEW)) isNew = false;
                if (!subtask.getStatus().equals(TaskStatusEnum.DONE)) isDone = false;
            }
            if (isNew) epic.setStatus(TaskStatusEnum.NEW);
            else if (isDone) epic.setStatus(TaskStatusEnum.DONE);
            else epic.setStatus(TaskStatusEnum.IN_PROGRESS);
        }
    }

    @Override
    public boolean update(int id, Task object) {
        setEpicStatusIfNotCorrect(object);
        return dao.update(id, object);
    }

    @Override
    public boolean remove(int id) {
        return dao.remove(id);
    }

    public Map<Integer, Subtask> getSubtasks(int id) {
        Optional<Task> taskOptional = dao.findById(id);
        if (taskOptional.isEmpty()) {
            String message = String.format("task with id = %s not found", id);
            throw new NoSuchElementException(message);
        } else {
            Task task = taskOptional.get();
            try {
                EpicTask epic = (EpicTask) task;
                return epic.getSubtasks();
            } catch (ClassCastException e) {
                throw new RuntimeException("There are no subtasks in task with id = " + id);
            }
        }
    }

    @Override
    public Map<Integer, Task> getAll() {
        return dao.getAll();
    }

    @Override
    public Optional<Subtask> findSubtaskById(int epicId, int supTaskId) {
        Optional<Task> taskOptional = findById(epicId);
        if (taskOptional.isEmpty()) {
            return Optional.empty();
        } else if (taskOptional.get() instanceof EpicTask epicTask) {
            Subtask task = epicTask.getSubtasks().get(supTaskId);
            if (task == null) {
                throw new NoSuchElementException("subtask with id = " + supTaskId + " not found");
            } else {
                return Optional.of(task);
            }
        } else {
            throw new RuntimeException("task with id = " + epicId + " exists, but not epic");
        }
    }

    @Override
    public void addSubtask(int epicId, Subtask object) {
        Optional<Task> epicOptional = dao.findById(epicId);
        if (epicOptional.isEmpty()) {
            throw new RuntimeException("Task with id = " + object.getId() + " not found");
        } else if (epicOptional.get() instanceof EpicTask epicTask) {
            if (epicTask.getSubtasks().containsKey(object.getId())) {
                throw new RuntimeException("Subtask with id = " + object.getId() + " is already exists");
            } else {
                epicTask.getSubtasks().put(epicId, object);
            }
        }
    }

    @Override
    public boolean updateSubtask(int epicId, int subTaskId, Subtask object) {
        Optional<Task> epicOptional = dao.findById(epicId);
        if (epicOptional.isEmpty()) {
            return false;
        } else {
            if (epicOptional.get() instanceof EpicTask epicTask) {
                if (epicTask.getSubtasks().containsKey(subTaskId)) {
                    Subtask subtask = epicTask.getSubtasks().get(subTaskId);
                    updateSubtask(subtask, object);
                    return true;
                }
            }
        }
        return false;
    }

    private void updateSubtask(Subtask subtask, Subtask object){
        subtask.setDescription(object.getDescription());
        subtask.setStatus(object.getStatus());
        subtask.setTitle(object.getTitle());
    }

    @Override
    public boolean removeSubtask(int epicId, int subTaskId) {
        Optional<Task> epicOptional = dao.findById(epicId);
        if (epicOptional.isEmpty()) {
            return false;
        } else {
            if (epicOptional.get() instanceof EpicTask epicTask) {
                if (epicTask.getSubtasks().containsKey(subTaskId)) {
                    epicTask.getSubtasks().remove(subTaskId);
                    return true;
                }
            }
        }
        return false;
    }
}
