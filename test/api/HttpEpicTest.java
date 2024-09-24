package api;

import api.token.EpicTypeToken;
import com.google.gson.Gson;
import data.Epic;
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


class HttpEpicTest {

    TaskManager manager = new InMemoryTaskManager();
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = taskServer.getGson();
    private static final String BASE_URL = "http://localhost:8080/epics";

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
    void getEpic() throws IOException, InterruptedException {
        Epic epic1 = new Epic(1, "Первый эпик", "Описание 1", Status.NEW,
                Duration.ofMinutes(15), LocalDateTime.of(2024, 7, 21, 11, 0),
                LocalDateTime.of(2024, 7, 21, 11, 15));
        Epic epic2 = new Epic(2, "Второй эпик", "Описание 2", Status.NEW,
                Duration.ofMinutes(15), LocalDateTime.of(2024, 7, 22, 11, 0),
                LocalDateTime.of(2024, 7, 22, 11, 15));
        Epic epic3 = new Epic(3, "Третий эпик", "Описание 3", Status.NEW,
                Duration.ofMinutes(15), LocalDateTime.of(2024, 7, 23, 11, 0),
                LocalDateTime.of(2024, 7, 23, 11, 15));


        manager.addEpic(epic1);
        manager.addEpic(epic2);
        manager.addEpic(epic3);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(BASE_URL);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Epic> fromManager = manager.getAllEpic();
        List<Epic> fromHttp = gson.fromJson(response.body(), new EpicTypeToken().getType());

        assertEquals(fromManager.size(), fromHttp.size());
        assertEquals(fromManager.get(0), fromHttp.get(0));
        assertEquals(fromManager.get(1), fromHttp.get(1));
        assertEquals(fromManager.get(2), fromHttp.get(2));
    }

    @Test
    void getEpicById() throws IOException, InterruptedException {
        Epic epic1 = new Epic(1, "epic1", "Description1", Status.NEW, Duration.ofMinutes(15),
                LocalDateTime.of(2024, 8, 24, 10, 0),
                LocalDateTime.of(2024, 8, 24, 10, 15));
        manager.addEpic(epic1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(BASE_URL + "/" + epic1.getId());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Epic taskByHttp = gson.fromJson(response.body(), Epic.class);

        assertEquals(200, response.statusCode());
        assertEquals(epic1, taskByHttp);
    }


    @Test
    void deleteEpic() throws IOException, InterruptedException {
        Epic epic1 = new Epic(1, "epic1", "Description1", Status.NEW, Duration.ofMinutes(15),
                LocalDateTime.of(2024, 8, 24, 10, 0),
                LocalDateTime.of(2024, 8, 24, 10, 15));
        manager.addEpic(epic1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(BASE_URL + "/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(204, response.statusCode());
        assertEquals(0, manager.getAllTask().size());
    }

    @Test
    void deleteEpicStatus500() throws IOException, InterruptedException {
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