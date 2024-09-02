package data;

import manager.InMemoryHistoryManager;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import util.Status;

class SubtaskTest {
TaskManager taskManager = new InMemoryTaskManager(new InMemoryHistoryManager());
    @Test
    void ifIdEqual() {
        Epic epic = new Epic("Testing", "Testy");

        Subtask subtask1 = new Subtask(1, "Test", Status.NEW, "Check", epic.getId());
        Subtask subtask2 = new Subtask(2, "Test2", Status.NEW, "Check2", epic.getId());
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        subtask1.setId(subtask2.getId());
        Assertions.assertEquals(subtask1, subtask2, "Error");
    }
}