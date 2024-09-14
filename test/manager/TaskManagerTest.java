package manager;

import data.Epic;
import data.Subtask;
import data.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;
    protected Task task;
    protected Epic epic;
    protected Subtask subtask;

    @BeforeEach
    void beforeEach() {
        task = new Task("NameTask", "Description task");
        task.setStartTime(LocalDateTime.now());
        task.setDuration(Duration.ofMinutes(20));

        epic = new Epic("Epic", "Epic's description");

        subtask = new Subtask("Subtask's name", "Subtask's description", 2);
        subtask.setStartTime(LocalDateTime.now().plusHours(1));
        subtask.setDuration(Duration.ofMinutes(20));
    }

    @Test
    void addTask() {
        taskManager.addTask(task);
        assertNotNull(taskManager.getAllTask());
        assertEquals(task, taskManager.getAllTask().getFirst());
        assertEquals(1, taskManager.getAllTask().size());
    }

    @Test
    void addEpic() {
        taskManager.addEpic(epic);
        assertNotNull(taskManager.getAllEpic());
        assertEquals(epic, taskManager.getAllEpic().getFirst());
        assertEquals(1, taskManager.getAllEpic().size());
    }

    @Test
    void addSubtask() {
        taskManager.addTask(task);
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);
        assertEquals(subtask.getEpicId(), epic.getId());
        assertEquals(subtask, taskManager.getAllSubtasks().getFirst());
        assertNotNull(taskManager.getAllSubtasks());
        assertEquals(1, taskManager.getAllSubtasks().size());
    }

    @Test
    void findTaskById() {
        taskManager.addTask(task);
        assertEquals(task, taskManager.findTaskById(task.getId()));
    }

    @Test
    void findEpicById() {
        taskManager.addEpic(epic);
        assertEquals(epic, taskManager.findEpicById(epic.getId()));
    }

    @Test
    void findSubTaskById() {
        taskManager.addTask(task);
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);
        assertEquals(subtask, taskManager.findSubtaskById(subtask.getId()));
    }

    @Test
    void checkOfRemovingOfTask() {
        taskManager.addTask(task);
        assertNotNull(taskManager.findTaskById(task.getId()));
        taskManager.removeTaskById(task.getId());
        assertEquals(0, taskManager.getAllTask().size());
    }

    @Test
    void checkOfRemovingOfSubTask() {
        taskManager.addTask(task);
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);
        taskManager.removeSubtaskById(subtask.getId());
        assertEquals(0, taskManager.getAllSubtasks().size());
    }

    @Test
    void checkOfRemovingOfEpic() {
        taskManager.addEpic(epic);
        assertNotNull(taskManager.findEpicById(epic.getId()));
        taskManager.removeEpicById(epic.getId());
        assertEquals(0, taskManager.getAllEpic().size());
    }

    @Test
    void checkOfPrioritized() {
        task.setStartTime(LocalDateTime.now());
        task.setDuration(Duration.ofMinutes(20));
        taskManager.addTask(task);
        taskManager.addEpic(epic);
        subtask.setStartTime(LocalDateTime.now().minusMinutes(60));
        subtask.setDuration(Duration.ofMinutes(15));
        subtask.setStatus(Status.NEW);
        taskManager.addSubtask(subtask);

        assertEquals(taskManager.getPrioritizedTasks(), List.of(subtask, task));
    }

    @Test
    void checkForIntersection() {
        epic.setStartTime(LocalDateTime.of(2024, 8, 25, 18, 0));
        epic.setDuration(Duration.ofMinutes(30));
        taskManager.addEpic(epic);
        task.setStartTime(LocalDateTime.now());
        task.setDuration(Duration.ofMinutes(60));
        taskManager.addTask(task);
        subtask.setStartTime(LocalDateTime.now().plusMinutes(30));
        subtask.setDuration(Duration.ofMinutes(60));

    }

    @Test
    void checkOfHistory() {
        taskManager.addTask(task);
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);
        taskManager.findEpicById(epic.getId());
        taskManager.findTaskById(task.getId());
        assertEquals(taskManager.getHistory(), List.of(task, epic));
    }

    @Test
    void shouldEqualsTasksWithSameId() {
        Task task1 = new Task("Описание 1", "Имя 1");
        Task task2 = new Task("Описание 2", "Имя 2");

        task2.setId(task1.getId());
        assertEquals(task1, task2);
    }
}
