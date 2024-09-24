package api.handlers;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

import static api.HttpTaskServer.gson;
import static api.HttpTaskServer.taskManager;

public class PrioritizedHandler extends BaseHttpHandler {

    @Override
    public void safeHandle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        String[] split = exchange.getRequestURI().getPath().split("/");

        if (requestMethod.equals("GET") && split[1].equals("prioritized")) {
            try {
                sendText(exchange, gson.toJson(taskManager.getPrioritizedTasks()));
            } catch (Exception e) {
                writeResponse(exchange, 500, "");
            }
        } else {
            writeResponse(exchange, 400, "");
        }
    }
}
