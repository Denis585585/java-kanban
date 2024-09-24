package api.handlers;
import com.sun.net.httpserver.HttpExchange;
import data.Epic;
import java.io.IOException;
import static api.HttpTaskServer.gson;
import static api.HttpTaskServer.taskManager;

public class EpicHandlers extends BaseHttpHandler {

    @Override
    public void safeHandle(HttpExchange exchange) throws IOException {
        Endpoints endpoint = getEndpoint(exchange);
        String[] split = exchange.getRequestURI().getPath().split("/");
        Epic epic;
        switch (endpoint) {
            case GET -> sendText(exchange, gson.toJson(taskManager.getAllEpic()));
            case GET_BY_ID -> sendText(exchange, gson.toJson(taskManager.findEpicById(Integer.parseInt(split[2]))));
            case GET_SUBS_BY_EPIC_ID ->
                    sendText(exchange, gson.toJson(taskManager.findAllSubtaskByEpicId(Integer.parseInt(split[2]))));
            case POST -> {
                epic = gson.fromJson(getTaskFromRequestBody(exchange), Epic.class);
                taskManager.addEpic(epic);
                writeResponse(exchange, 201, "Задача добавлена");
            }
            case POST_BY_ID -> {
                epic = gson.fromJson(getTaskFromRequestBody(exchange), Epic.class);
                taskManager.updateEpic(epic);
                sendText(exchange, "");
            }
            case DELETE_BY_ID -> {
                epic = taskManager.findEpicById(Integer.parseInt(split[2]));
                taskManager.removeEpicById(epic.getId());
                writeResponse(exchange, 204, "");
            }
            case UNKNOWN -> sendNotFound(exchange);
        }
    }
}

