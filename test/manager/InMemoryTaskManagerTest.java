package manager;

import data.Epic;
import data.Subtask;
import data.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.Status;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryTaskManagerTest {

    InMemoryTaskManager taskManager;
    Epic epic;
    Task task;
    Subtask subtask;

    @BeforeEach
    void beforeEach() {
        taskManager = new InMemoryTaskManager(new InMemoryHistoryManager());
        task = new Task( "Task",  "Task description");
        epic = new Epic( "Title", "Epic description1");
        subtask = new Subtask("Subtask", "Subtask description", epic.getId());
    }

    @Test
    void shouldAddTaskAndFindById() {
        taskManager.addTask(task);

        Assertions.assertNotNull(taskManager.findTaskById(task.getId()));
        Assertions.assertNotNull(taskManager.getAllTask());
    }

    @Test
    void shouldAddEpicAndFindById() {
        taskManager.addEpic(epic);

        Assertions.assertNotNull(taskManager.findEpicById(epic.getId()));
        Assertions.assertNotNull(taskManager.getAllEpic());
    }

    @Test
    void shouldAddSubtaskAndFindById() {
        taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask(1, "What", Status.NEW, "Is it", epic.getId());
        taskManager.addSubtask(subtask1);

        Assertions.assertNotNull(taskManager.findSubtaskById(subtask1.getId()));
        Assertions.assertNotNull(taskManager.getAllSubtasks());
    }
    @Test
    void shouldEqualsTasksWithSameId() {
        Task task1 = new Task("Описание 1", "Имя 1");
        Task task2 = new Task("Описание 2", "Имя 2");

        task2.setId(task1.getId());
        assertEquals(task1, task2);
    }
}