package manager;

import data.Epic;
import data.Subtask;
import data.Task;
import util.Status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private final InMemoryHistoryManager defaultHistory;
    private int nextId = 1;
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public InMemoryTaskManager(InMemoryHistoryManager inMemoryHistoryManager) {
        this.defaultHistory = Managers.getDefaultHistory();
    }


    private int generateId() {
        return nextId++;
    }

    @Override
    public void addTask(Task task) {
        int newId = generateId();
        task.setId(newId);
        tasks.put(task.getId(), task);
    }

    @Override
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

    @Override
    public void addEpic(Epic epic) {
        int newId = generateId();
        epic.setId(newId);
        epics.put(epic.getId(), epic);
    }

    private void updateEpicStatus(Epic epic) {
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

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            Epic actualEpic = epics.get(epic.getId());
            actualEpic.setTitle(epic.getTitle());
            actualEpic.setDescription(epic.getDescription());
        }
    }

    @Override
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

    @Override
    public void clearTasks() {
        tasks.clear();
    }

    @Override
    public void clearEpics() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void clearSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.clearSubtask();
            updateEpicStatus(epic);
        }
    }

    @Override
    public Task findTaskById(int id) {
        defaultHistory.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Epic findEpicById(int id) {
        defaultHistory.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public Subtask findSubtaskById(int id) {
        defaultHistory.add(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public void removeTaskById(int id) {
        tasks.remove(id);
    }

    @Override
    public void removeEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            for (Integer subtask : epic.getSubtaskId()) {
                subtasks.remove(subtask);
            }
        }
        epics.remove(id);
    }

    @Override
    public void removeSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            epic.removeSubtask(id);
            updateEpicStatus(epic);
            subtasks.remove(id);
        }
    }

    @Override
    public ArrayList<Task> showAllTask() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> showAllEpic() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Subtask> showAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
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

    @Override
    public List<Task> getHistory() {
        return defaultHistory.getHistory();
    }
}

