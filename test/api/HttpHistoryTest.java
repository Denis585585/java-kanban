package api;

import api.token.TaskTypeToken;
import com.google.gson.Gson;
import data.Epic;
import data.Subtask;
import data.Task;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.Status;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HttpHistoryTest {
    TaskManager manager = new InMemoryTaskManager();
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = taskServer.getGson();
    private static final String BASE_URL = "http://localhost:8080/history";

    @BeforeEach
    void setUp() {
        manager.clearTasks();
        manager.clearSubtasks();
        manager.clearEpics();
        taskServer.start();
    }

    @AfterEach
    void shutDown() {
        taskServer.stop();
    }

    @Test
    void getHistory() throws IOException, InterruptedException {
        Task task1 = new Task(1, "task", "description", Status.NEW,
                Duration.ofMinutes(15), LocalDateTime.of(2024, 8, 24, 20, 40));
        Epic epic2 = new Epic(1, "epic", "description", Status.NEW, Duration.ofMinutes(15),
                LocalDateTime.of(2024, 8, 24, 10, 0),
                LocalDateTime.of(2024, 8, 24, 10, 15));
        Subtask subtask3 = new Subtask(4, "subtask", "description", Status.NEW,
                Duration.ofMinutes(15),
                LocalDateTime.of(2024, 8, 26, 20, 40), 2);

        manager.addTask(task1);
        manager.addEpic(epic2);
        manager.addSubtask(subtask3);

        manager.findEpicById(epic2.getId());
        manager.findTaskById(task1.getId());
        manager.findSubtaskById(subtask3.getId());

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(BASE_URL);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> historyByHttp = gson.fromJson(response.body(), new TaskTypeToken().getType());

        assertEquals(200, response.statusCode());
        assertEquals(3, historyByHttp.size());
        assertEquals(task1.getId(), historyByHttp.get(1).getId());
    }
}