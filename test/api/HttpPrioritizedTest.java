package api;

import api.token.TaskTypeToken;
import com.google.gson.Gson;
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

class HttpPrioritizedTest {
    TaskManager manager = new InMemoryTaskManager();
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = taskServer.getGson();
    private static final String BASE_URL = "http://localhost:8080/prioritized";

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
    void getPrioritized() throws IOException, InterruptedException {
        Task task1 = new Task(1, "task1", "description1", Status.NEW,
                Duration.ofMinutes(15), LocalDateTime.of(2024, 8, 25, 20, 40));
        Task task2 = new Task(2, "task2", "description2", Status.NEW,
                Duration.ofMinutes(15), LocalDateTime.of(2024, 8, 24, 20, 40));
        Task task3 = new Task(3, "task3", "description3", Status.NEW,
                Duration.ofMinutes(15), LocalDateTime.of(2024, 8, 26, 20, 40));

        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(task3);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(BASE_URL);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> prioritizedByHttp = gson.fromJson(response.body(), new TaskTypeToken().getType());

        assertEquals(task2, prioritizedByHttp.get(0));
        assertEquals(task1, prioritizedByHttp.get(1));
        assertEquals(task3, prioritizedByHttp.get(2));
    }

}