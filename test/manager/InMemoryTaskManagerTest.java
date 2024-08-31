package manager;

import data.Epic;
import data.Subtask;
import data.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.Status;

class InMemoryTaskManagerTest {

    InMemoryTaskManager taskManager;
    Epic epic;
    Task task;
    Subtask subtask;

    @BeforeEach
    void beforeEach() {
        taskManager = new InMemoryTaskManager(new InMemoryHistoryManager());
        epic = new Epic(1, "Epic1", Status.NEW, "Epic description1");
        task = new Task(2, "Task", Status.NEW, "Task description");
        subtask = new Subtask(3, "Subtask", Status.NEW, "Subtask description", epic.getId());
    }

    @Test
    void shouldAddTaskAndFindById() {
        taskManager.addTask(task);

        Assertions.assertNotNull(taskManager.findTaskById(task.getId()));
        Assertions.assertNotNull(taskManager.showAllTask());
    }

    @Test
    void shouldAddEpicAndFindById() {
        taskManager.addEpic(epic);

        Assertions.assertNotNull(taskManager.findEpicById(epic.getId()));
        Assertions.assertNotNull(taskManager.showAllEpic());
    }

    @Test
    void shouldAddSubtaskAndFindById() {
        taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask(1, "What", Status.NEW, "Is it", epic.getId());
        taskManager.addSubtask(subtask1);

        Assertions.assertNotNull(taskManager.findSubtaskById(subtask1.getId()));
        Assertions.assertNotNull(taskManager.showAllSubtasks());
    }
}