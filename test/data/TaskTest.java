package data;

import manager.InMemoryHistoryManager;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import util.Status;

class TaskTest {
    TaskManager taskManager = new InMemoryTaskManager(new InMemoryHistoryManager());

    @Test
    void ifIdEqual() {
        Task task1 = new Task(1, "Test", Status.NEW, "Check");
        Task task2 = new Task(1, "Test2", Status.NEW, "Check2");
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        task1.setId(task2.getId());
        Assertions.assertEquals(task1, task2, "Error");
    }


}