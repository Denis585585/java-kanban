package manager;

import data.Epic;
import data.Subtask;
import data.Task;
import exceptions.ManagerSaveException;
import util.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private final InMemoryHistoryManager defaultHistory;
    protected int nextId = 1;
    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, Epic> epics = new HashMap<>();
    protected HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected Set<Task> priority = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    public InMemoryTaskManager(InMemoryHistoryManager inMemoryHistoryManager) {
        this.defaultHistory = Managers.getDefaultHistory();
    }

    public InMemoryTaskManager() {
        defaultHistory = new InMemoryHistoryManager();
    }


    private int generateId() {
        return nextId++;
    }


    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(priority);
    }

    private void updateEpicTime(Epic epic) {
        List<Task> subtaskList = getPrioritizedTasks().stream()
                .filter(task -> task instanceof Subtask)
                .filter(task -> ((Subtask) task).getEpicId() == epic.getId())
                .toList();
        if (subtaskList.isEmpty()) {
            return;
        }
        Duration duration = Duration.ofMinutes(0);
        for (Task subtask : subtaskList) {
            duration = duration.plus(subtask.getDuration());
        }
        LocalDateTime startTime = subtaskList.getFirst().getStartTime();
        LocalDateTime endTime = subtaskList.getLast().getEndTime();
        epic.setStartTime(startTime);
        epic.setEndTime(endTime);
        epic.setDuration(duration);
    }

    private void addPrioritizedTask(Task task) {
        if (task instanceof Epic) return;
        List<Task> taskList = getPrioritizedTasks();
        if (task.getStartTime() != null && task.getEndTime() != null) {
            for (Task task1 : taskList) {
                if (task1.getId() == task.getId()) priority.remove(task1);
                if (checkIntersection(task, task1)) {
                    return;
                }
            }
            priority.add(task);
        }
    }

    private boolean checkIntersection(Task task1, Task task2) {
        return !task1.getEndTime().isBefore(task2.getStartTime()) &&
                !task1.getStartTime().isAfter(task2.getEndTime());
    }

    private void confirmPriority(Task task) {
        if (task == null || task.getStartTime() == null) return;
        List<Task> taskList = getPrioritizedTasks();
        for (Task task1 : taskList) {
            if (task1 == task) {
                continue;
            }
            boolean taskIntersection = checkIntersection(task, task1);
            if (taskIntersection) {
                throw new ManagerSaveException("Найдено пересечение задач " + task.getId()
                        + " и " + task1.getId());
            }
        }
    }

    @Override
    public void addTask(Task task) {
        int newId = generateId();
        task.setId(newId);
        confirmPriority(task);
        addPrioritizedTask(task);
        tasks.put(task.getId(), task);
    }

    @Override
    public void addSubtask(Subtask subtask) {
        int newId = generateId();
        subtask.setId(newId);
        Epic epic = epics.get(subtask.getEpicId());
        confirmPriority(subtask);
        addPrioritizedTask(subtask);
        if (epic != null) {
            epic.setSubtaskId(newId);
            subtasks.put(subtask.getId(), subtask);
            updateEpicStatus(epic);
            updateEpicTime(epic);
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
        updateEpicTime(epic);
        confirmPriority(epic);
        addPrioritizedTask(subtask);
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
            updateEpicTime(epic);
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
                priority.remove(subtasks.get(subtask));
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
            priority.remove(subtask);
            subtasks.remove(id);
        }
    }

    @Override
    public ArrayList<Task> getAllTask() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> getAllEpic() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Subtask> getAllSubtasks() {
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

