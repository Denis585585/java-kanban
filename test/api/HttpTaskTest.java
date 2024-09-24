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
import static org.junit.jupiter.api.Assertions.assertNotNull;

class HttpTaskTest {

    TaskManager manager = new InMemoryTaskManager();
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = taskServer.getGson();
    private static final String BASE_URL = "http://localhost:8080/tasks";

    @BeforeEach
    void setUp() {
        manager.clearTasks();
        manager.clearSubtasks();
        manager.clearEpics();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    void getTask() throws IOException, InterruptedException {
        Task task1 = new Task(1, "task1", "description1", Status.NEW,
                Duration.ofMinutes(15), LocalDateTime.of(2024, 8, 24, 20, 40));
        Task task2 = new Task(2, "task2", "description2", Status.NEW,
                Duration.ofMinutes(15), LocalDateTime.of(2024, 8, 25, 20, 40));
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
        List<Task> fromManager = manager.getAllTask();
        List<Task> fromHttp = gson.fromJson(response.body(), new TaskTypeToken().getType());

        assertEquals(fromManager.size(), fromHttp.size());
        assertEquals(fromManager.get(0), fromHttp.get(0));
        assertEquals(fromManager.get(1), fromHttp.get(1));
        assertEquals(fromManager.get(2), fromHttp.get(2));
    }

    @Test
    void getTaskById() throws IOException, InterruptedException {
        Task task3 = new Task(3, "task3", "description3", Status.NEW,
                Duration.ofMinutes(15), LocalDateTime.of(2024, 8, 26, 20, 40));
        manager.addTask(task3);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(BASE_URL + "/" + task3.getId());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Task taskByHttp = gson.fromJson(response.body(), Task.class);

        assertEquals(200, response.statusCode());
        assertEquals(task3, taskByHttp);
    }

    @Test
    void testAddTask() throws IOException, InterruptedException {
        Task task = new Task("Test 2", "Testing task 2");
        task.setDuration(Duration.ofMinutes(5));
        task.setStartTime(LocalDateTime.now());

        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        List<Task> tasksFromManager = manager.getAllTask();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 2", tasksFromManager.getFirst().getTitle(), "Некорректное имя задачи");
    }


    @Test
    void testStatusCode200() throws IOException, InterruptedException {
        Task task1 = new Task(1, "task1", "description1", Status.NEW,
                Duration.ofMinutes(15), LocalDateTime.of(2024, 8, 24, 20, 40));
        Task newTask1 = new Task(2, "task2", "description2", Status.IN_PROGRESS,
                Duration.ofMinutes(15), LocalDateTime.of(2024, 8, 25, 20, 40));
        manager.addTask(task1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(BASE_URL + "/" + task1.getId());
        String taskJson = gson.toJson(newTask1);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    void deleteTask() throws IOException, InterruptedException {
        Task task1 = new Task("task1", "description1");
        manager.addTask(task1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(BASE_URL + "/" + task1.getId());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(204, response.statusCode());
        assertEquals(0, manager.getAllTask().size());
    }

    @Test
    void deleteTaskStatus500() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(BASE_URL + "/2");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(500, response.statusCode());
    }
}