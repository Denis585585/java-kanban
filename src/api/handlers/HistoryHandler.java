package api.handlers;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

import static api.HttpTaskServer.gson;
import static api.HttpTaskServer.taskManager;

public class HistoryHandler extends BaseHttpHandler {


    @Override
    public void safeHandle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        String[] split = exchange.getRequestURI().getPath().split("/");

        if (requestMethod.equals("GET") && split[1].equals("history")) {
            try {
                sendText(exchange, gson.toJson(taskManager.getHistory()));
            } catch (Exception e) {
                writeResponse(exchange, 500, "");
            }
        } else {
            writeResponse(exchange, 400, "");
        }
    }
}