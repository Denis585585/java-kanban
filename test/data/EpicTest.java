package data;

import manager.InMemoryHistoryManager;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import util.Status;

class EpicTest {

    TaskManager taskManager = new InMemoryTaskManager(new InMemoryHistoryManager());

    @Test
    void ifIdEqual() {
        Epic epic1 = new Epic(1, "Test", Status.NEW, "Check");
        Epic epic2 = new Epic(2, "Test2", Status.NEW, "Check2");
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        epic1.setId(epic2.getId());
        Assertions.assertEquals(epic1, epic2, "Error");
    }
}