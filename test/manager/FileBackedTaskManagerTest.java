package manager;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FileBackedTaskManagerTest extends TaskManagersTest<FileBackedTaskManager> {

    File file;

    FileBackedTaskManagerTest() throws IOException {
        file = File.createTempFile("test", ".csv");
        taskManager = new FileBackedTaskManager(file);
    }


    @Test
    void testLoadFromFile() {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);

        task.setStartTime(LocalDateTime.now());
        task.setDuration(Duration.ofMinutes(20));
        fileBackedTaskManager.addTask(task);

        fileBackedTaskManager.addEpic(epic);

        subtask.setStartTime(LocalDateTime.now().plusMinutes(30));
        subtask.setDuration(Duration.ofMinutes(20));
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