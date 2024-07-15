package data;

import manager.InMemoryHistoryManager;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TaskTest {
    TaskManager taskManager = new InMemoryTaskManager();

    @Test
    void ifIdEqual() {
        Task task1 = new Task("Test", "Check");
        Task task2 = new Task("Test2", "Check2");
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        task1.setId(task2.getId());
        Assertions.assertEquals(task1, task2, "Error");
    }


}