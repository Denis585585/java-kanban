package api.handlers;

import com.sun.net.httpserver.HttpExchange;
import data.Subtask;

import java.io.IOException;

import static api.HttpTaskServer.gson;
import static api.HttpTaskServer.taskManager;

public class SubtaskHandlers extends BaseHttpHandler {


    @Override
    public void safeHandle(HttpExchange exchange) throws IOException {
        Endpoints endpoint = getEndpoint(exchange);
        String[] split = exchange.getRequestURI().getPath().split("/");
        Subtask subtask;
        switch (endpoint) {
            case GET -> sendText(exchange, gson.toJson(taskManager.getAllSubtasks()));
            case GET_BY_ID -> sendText(exchange, gson.toJson(taskManager.findSubtaskById(Integer.parseInt(split[2]))));
            case POST -> {
                subtask = gson.fromJson(getTaskFromRequestBody(exchange), Subtask.class);
                taskManager.addSubtask(subtask);
                writeResponse(exchange, 201, "Задача добавлена");
            }
            case POST_BY_ID -> {
                subtask = gson.fromJson(getTaskFromRequestBody(exchange), Subtask.class);
                taskManager.updateSubtask(subtask);
                sendText(exchange, "");
            }
            case DELETE_BY_ID -> {
                subtask = taskManager.findSubtaskById(Integer.parseInt(split[2]));
                taskManager.removeSubtaskById(subtask.getId());
                writeResponse(exchange, 204, "");
            }
            case UNKNOWN -> sendNotFound(exchange);
        }
    }
}
