package data;

import manager.InMemoryHistoryManager;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {
TaskManager taskManager = new InMemoryTaskManager();
    @Test
    void ifIdEqual() {
        Epic epic = new Epic("Epic1", "2133");

        Subtask subtask1 = new Subtask("Test", "Check", epic.getId());
        Subtask subtask2 = new Subtask("Test2", "Check2", epic.getId());
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        subtask1.setId(subtask2.getId());
        Assertions.assertEquals(subtask1, subtask2, "Error");
    }
}