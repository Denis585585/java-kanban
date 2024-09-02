package manager;

import static org.junit.jupiter.api.Assertions.*;

import util.Status;
import data.Task;
import data.Subtask;
import data.Epic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.File;

class FileBackedTaskManagerTest {

    private File file;
    private Task task;
    private Epic epic;
    private Subtask subtask;

    @BeforeEach
    void beforeEach() throws IOException {
        file = File.createTempFile("test", ".csv");
        task = new Task("testTask", "testTaskDescription", Status.IN_PROGRESS);
        epic = new Epic("testEpic", "testEpicDescription", Status.IN_PROGRESS);
        subtask = new Subtask("testSub", "testSubDescription", Status.IN_PROGRESS,  2);
    }

    @Test
    void testLoadFromFile() {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);

        fileBackedTaskManager.addTask(task);
        fileBackedTaskManager.addEpic(epic);
        fileBackedTaskManager.addSubtask(subtask);

        assertEquals(1, fileBackedTaskManager.tasks.size());
        assertEquals(1, fileBackedTaskManager.epics.size());
        assertEquals(1, fileBackedTaskManager.subtasks.size());

        FileBackedTaskManager fileManager = FileBackedTaskManager.loadFromFile(file);

        assertEquals(fileBackedTaskManager.getAllTask(), fileManager.getAllTask());
        assertEquals(fileBackedTaskManager.getAllEpic(), fileManager.getAllEpic());
        assertEquals(fileBackedTaskManager.getAllSubtasks(), fileManager.getAllSubtasks());
    }

    @Test
    void loadFromEmptyFile() throws IOException {
        File emptyFile = File.createTempFile("emptyTest", ".csv");
        FileBackedTaskManager loadFile = FileBackedTaskManager.loadFromFile(emptyFile);
        assertNotNull(loadFile);
        assertEquals(0, loadFile.epics.size());
        assertEquals(0, loadFile.subtasks.size());
        assertEquals(0, loadFile.tasks.size());
    }
}