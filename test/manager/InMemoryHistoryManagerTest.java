package manager;
import data.Epic;
import data.Subtask;
import data.Task;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import util.Status;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    InMemoryHistoryManager history = new InMemoryHistoryManager();
    static Task task;
    static Epic epic;
    static Subtask subtask;

    @BeforeAll
    static void beforeAll() {
        task = new Task(1, "Title", Status.NEW, "New description");
        epic = new Epic(2, "Epic", Status.NEW, "Epic's name");
        subtask = new Subtask(3, "Subtask", Status.NEW, "Subtask's name", 3);

        task.setId(1);
        epic.setId(2);
        subtask.setId(3);
    }

    @Test
    void add() {
        history.add(task);

        assertNotNull(history.getHistory());
        assertEquals(history.getHistory().getFirst(), task);
    }

    @Test
    void removeFirstInTaskHistory() {
        history.add(task);
        history.add(epic);
        history.add(subtask);

        history.remove(subtask.getId());

        assertEquals(history.getHistory(), List.of(epic, task));
    }

    @Test
    void uniquenessSubtask() {
        history.add(task);
        history.add(epic);
        history.add(subtask);
        history.add(task);

        assertNotEquals(task, history.getHistory().getLast());
    }
}