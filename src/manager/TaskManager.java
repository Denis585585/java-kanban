package manager;

import data.Epic;
import data.Subtask;
import data.Task;

import java.util.ArrayList;

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

    ArrayList<Task> showAllTask();

    ArrayList<Epic> showAllEpic();

    ArrayList<Subtask> showAllSubtasks();

    ArrayList<Subtask> findAllSubtaskByEpicId(int id);
}
