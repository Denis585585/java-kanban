package Manager;

import Data.Epic;
import Data.Subtask;
import Data.Task;
import util.Status;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    Limits limit;
    private int nextId = 1;
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public TaskManager(Limits limit) {
        this.limit = limit;
    }

    public int generateId() {
        return nextId++;
    }

    public void addTask(Task task) {
        int newId = generateId();
        task.setId(newId);
        tasks.put(task.getId(), task);
    }

    public void addSubtask(Subtask subtask) {
        int newId = generateId();
        subtask.setId(newId);
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            epic.setSubtaskId(newId);
            subtasks.put(subtask.getId(), subtask);
            updateEpicStatus(epic);
        }
    }

    public void addEpic(Epic epic) {
        int newId = generateId();
        epic.setId(newId);
        epics.put(epic.getId(), epic);
    }

    public void updateEpicStatus(Epic epic) {
        if (!epics.containsKey(epic.getId())) {
            return;
        }

        ArrayList<Subtask> epicSubtasks = new ArrayList<>();

        int countNew = 0;
        int countDone = 0;

        for (int i = 0; i < epic.getSubtaskId().size(); i++) {
            epicSubtasks.add(subtasks.get(epic.getSubtaskId().get(i)));
        }

        for (Subtask subtask : epicSubtasks) {
            if (subtask.getStatus().equals(Status.DONE)) {
                countDone++;
            } else if (subtask.getStatus().equals(Status.NEW)) {
                countNew++;
            } else {
                epic.setStatus(Status.IN_PROGRESS);
                return;
            }

            if (countNew == epicSubtasks.size()) {
                epic.setStatus(Status.NEW);
            } else if (countDone == epicSubtasks.size()) {
                epic.setStatus(Status.DONE);
            } else {
                epic.setStatus(Status.IN_PROGRESS);
            }
        }
    }

    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            Epic actualEpic = epics.get(epic.getId());
            actualEpic.setTitle(epic.getTitle());
            actualEpic.setDescription(epic.getDescription());
        }
    }

    public void updateSubtask(Subtask subtask) {
        if ((subtask == null || (!subtasks.containsKey(subtask.getId())))) {
            return;
        }
        Epic epic = epics.get(subtask.getEpicId());
        if (epic == null) {
            return;
        }
        subtasks.replace(subtask.getId(), subtask);
        updateEpicStatus(epic);
    }

    public void clearTasks() {
        tasks.clear();
    }

    public void clearEpics() {
        epics.clear();
        subtasks.clear();
    }

    public void clearSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.clearSubtask();
            updateEpicStatus(epic);
        }
    }

    public Task findTaskById(int id) {
        limit.add(tasks.get(id));
        return tasks.get(id);
    }

    public Epic findEpicById(int id) {
        limit.add(epics.get(id));
        return epics.get(id);
    }

    public Subtask findSubtaskById(int id) {
        limit.add(subtasks.get(id));
        return subtasks.get(id);
    }

    public void removeTaskById(int id) {
        tasks.remove(id);
    }

    public void removeEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            for (Integer subtask : epic.getSubtaskId()) {
                subtasks.remove(subtask);
            }
        }
        epics.remove(id);
    }

    public void removeSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            epic.removeSubtask(id);
            updateEpicStatus(epic);
            subtasks.remove(id);
        }
    }

    public ArrayList<Task> showAllTask() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> showAllEpic() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Subtask> showAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public ArrayList<Subtask> findAllSubtaskByEpicId(int id) {
        ArrayList<Subtask> thisSubtasks = new ArrayList<>();
        Epic epic = epics.get(id);
        if (epic != null) {
            for (Integer subtaskId : epic.getSubtaskId()) {
                thisSubtasks.add(subtasks.get(subtaskId));
            }
        }
        return thisSubtasks;
    }

    public ArrayList<Task> getLimit() {
        return limit.getLimit();

    }
}

