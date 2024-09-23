package api.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import data.Epic;
import data.Subtask;
import data.Task;
import exceptions.ManagerSaveException;
import manager.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public abstract class BaseHttpHandler implements HttpHandler {

    protected TaskManager taskManager;
    protected Gson gson;
    protected Task task;
    protected Epic epic;
    protected Subtask subtask;

    protected enum Endpoints {
        GET,
        GET_BY_ID,
        GET_SUBS_BY_EPIC_ID,
        POST,
        POST_BY_ID,
        DELETE_BY_ID,
        UNKNOWN
    }

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public BaseHttpHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            safeHandle(exchange);
        } catch (NullPointerException e) {
            sendNotFound(exchange);
        } catch (ManagerSaveException e) {
            sendHasInteractions(exchange);
        } catch (Exception e) {
            writeResponse(exchange, 500, "");
        }
    }

    public abstract void safeHandle(HttpExchange exchange) throws IOException;

    protected Endpoints getEndpoint(HttpExchange exchange) {
        String[] path = exchange.getRequestURI().getPath().split("/");
        String requestMethod = exchange.getRequestMethod();

        switch (requestMethod) {
            case "GET": {
                if (path.length == 3 && isNum(path[2])) {
                    return Endpoints.GET_BY_ID;
                } else if (path.length == 4 && path[1].equals("epics") && path[3].equals("subtasks")) {
                    return Endpoints.GET_SUBS_BY_EPIC_ID;
                }
                return Endpoints.GET;
            }
            case "POST": {
                if (path.length == 3 && isNum(path[2])) {
                    return Endpoints.POST_BY_ID;
                }
                return Endpoints.POST;
            }
            case "DELETE": {
                if (isNum(path[2])) {
                    return Endpoints.DELETE_BY_ID;
                }
            }
            default: {
                return Endpoints.UNKNOWN;
            }
        }
    }

    protected void writeResponse(HttpExchange exchange,
                                 int responseCode,
                                 String responseString) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(responseCode, -1);
            os.write(responseString.getBytes(DEFAULT_CHARSET));
        }
        exchange.close();
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] response = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(200, response.length);
        h.getResponseBody().write(response);
        h.close();
    }

    protected void sendNotFound(HttpExchange h) throws IOException {
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(404, 0);
        h.close();
    }

    protected void sendHasInteractions(HttpExchange h) throws IOException {
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(406, 0);
        h.close();
    }

    protected String getTaskFromRequestBody(HttpExchange exchange) throws IOException {
        try (InputStream is = exchange.getRequestBody()) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            writeResponse(exchange, 500, "");
            throw e;
        }
    }

    private boolean isNum(String number) {
        try {
            Integer.parseInt(number);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

