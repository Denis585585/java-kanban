package manager;

import data.Epic;
import data.Subtask;
import data.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    void addTask(Task task);

    void addSubtask(Subtask subtask);

    void addEpic(Epic epic);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    void clearTasks();

    void clearEpics();

    void clearSubtasks();

    Task findTaskById(int id);

    Epic findEpicById(int id);

    Subtask findSubtaskById(int id);

    void removeTaskById(int id);

    void removeEpicById(int id);

    void removeSubtaskById(int id);

    ArrayList<Task> getAllTask();

    ArrayList<Epic> getAllEpic();

    ArrayList<Subtask> getAllSubtasks();

    ArrayList<Subtask> findAllSubtaskByEpicId(int id);

    List<Task> getHistory();
    List<Task> getPrioritizedTasks();
}
