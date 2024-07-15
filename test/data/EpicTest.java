package data;

import manager.InMemoryHistoryManager;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EpicTest {

    TaskManager taskManager = new InMemoryTaskManager();

    @Test
    void ifIdEqual() {
        Epic epic1 = new Epic("Test", "Check");
        Epic epic2 = new Epic("Test2", "Check2");
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        epic1.setId(epic2.getId());
        Assertions.assertEquals(epic1, epic2, "Error");
    }
}