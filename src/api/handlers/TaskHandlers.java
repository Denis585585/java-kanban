package api.handlers;

import com.sun.net.httpserver.HttpExchange;
import data.Task;

import java.io.IOException;

import static api.HttpTaskServer.gson;
import static api.HttpTaskServer.taskManager;

public class TaskHandlers extends BaseHttpHandler {


    @Override
    public void safeHandle(HttpExchange exchange) throws IOException {
        Endpoints endpoint = getEndpoint(exchange);
        String[] split = exchange.getRequestURI().getPath().split("/");
        Task task;
        switch (endpoint) {
            case GET -> sendText(exchange, gson.toJson(taskManager.getAllTask()));
            case GET_BY_ID -> sendText(exchange, gson.toJson(taskManager.findTaskById(Integer.parseInt(split[2]))));
            case POST -> {
                task = gson.fromJson(getTaskFromRequestBody(exchange), Task.class);
                taskManager.addTask(task);
                writeResponse(exchange, 201, "Задача добавлена");
            }
            case POST_BY_ID -> {
                task = gson.fromJson(getTaskFromRequestBody(exchange), Task.class);
                taskManager.updateTask(task);
                sendText(exchange, "");
            }
            case DELETE_BY_ID -> {
                task = taskManager.findTaskById(Integer.parseInt(split[2]));
                taskManager.removeTaskById(task.getId());
                writeResponse(exchange, 204, "");
            }
            case UNKNOWN -> sendNotFound(exchange);
        }
    }
}
