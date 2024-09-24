package api;

import api.adapters.DurationAdapter;
import api.adapters.LocalDateTimeAdapter;
import api.handlers.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import manager.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    public static Gson gson;
    public static TaskManager taskManager;
    protected static HttpServer server;
    private static final int PORT = 8080;


    public HttpTaskServer(TaskManager taskManager) {
        HttpTaskServer.taskManager = taskManager;
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
        try {
            server = HttpServer.create(new InetSocketAddress(PORT), 0);
            server.createContext("/tasks", new TaskHandlers());
            server.createContext("/epics", new EpicHandlers());
            server.createContext("/subtasks", new SubtaskHandlers());
            server.createContext("/history", new HistoryHandler());
            server.createContext("/prioritized", new PrioritizedHandler());
        } catch (IOException e) {
            throw new RuntimeException("Ошибка создания http-сервера на порте - " + PORT);
        }
    }

    public Gson getGson() {
        return gson;
    }

    public void start() {
        System.out.println("Сервер запушен на порту " + PORT);
        System.out.println("http://localhost:" + PORT + "/");
        server.start();
    }

    public void stop() {
        System.out.println("Сервер на порту " + PORT + " был остановлен");
        server.stop(0);
    }

}
