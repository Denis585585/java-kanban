package manager;

import data.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class InMemoryHistoryManagerTest {

    InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
    static Task task;
    static ArrayList<Task> history;

    @BeforeAll
    static void beforeAll() {
        task = new Task("Title", "New description");
        history = new ArrayList<>();
    }

    @Test
    void addAndShouldGetHistory() {
        historyManager.add(task);
        history = (ArrayList<Task>) historyManager.getHistory();

        Assertions.assertNotNull(history);
        Assertions.assertEquals(1, history.size());
    }

    @Test
    void shouldReturn10TasksWhenAddMore() {

        for (int i = 0; i < 15; i++) {
            historyManager.add(task);
        }
        history = (ArrayList<Task>) historyManager.getHistory();
        Assertions.assertEquals(10, history.size());
    }
}