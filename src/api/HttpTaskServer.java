package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import api.adapters.DurationAdapter;
import api.adapters.LocalDataTimeAdapter;
import api.handlers.*;
import manager.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;

public class HttpTaskServer {

    protected static Gson gson;
    private static TaskManager taskManager;
    protected static HttpServer server;
    private static final int PORT = 8080;


    public static void main(String[] args) {
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDataTimeAdapter())
                .registerTypeAdapter(LocalDateTime.class, new DurationAdapter())
                .create();
        try {
            server = HttpServer.create(new InetSocketAddress(PORT), 0);
            server.createContext("/tasks", new TaskHandlers(taskManager, gson));
            server.createContext("/epics", new EpicHandlers(taskManager, gson));
            server.createContext("/subtasks", new SubtaskHandlers(taskManager, gson));
            server.createContext("/history", new HistoryHandler(taskManager, gson));
            server.createContext("/prioritized", new PrioritizedHandler(taskManager, gson));
        } catch (IOException e) {
            throw new RuntimeException("При создании сервера на порте - " + PORT + "произошла ошибка.");
        }
        HttpTaskServer.start();
       // HttpTaskServer.stop();

    }

    public static Gson getGson() {
        return gson;
    }

    public static void setTaskManager(TaskManager taskManager) {
        HttpTaskServer.taskManager = taskManager;
    }

    public static void start() {
        System.out.println("Сервер запушен на порту " + PORT);
        System.out.println("http://localhost:" + PORT + "/");
        server.start();
    }

    public static void stop() {
        System.out.println("Сервер на порту " + PORT + " был остановлен");
        server.stop(0);
    }

}
