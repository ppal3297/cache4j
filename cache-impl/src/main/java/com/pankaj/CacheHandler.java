package com.pankaj;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class CacheHandler implements HttpHandler {
    private final int ttl = 600000;
    private final ConcurrentHashMap<String, CacheValue> store = new ConcurrentHashMap<>();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        switch(method){
            case "PUT" ->
            handlePut(exchange);
            case "GET" ->
            handleGet(exchange);
            default -> 
            sendResponse(exchange, 405, "Method Not Allowed");
        }
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        String key = query != null ? query.split("=")[1] : null;
        if (key == null || !store.containsKey(key)) {
            sendResponse(exchange, 400, "missing key");
            return;
        }
        CacheValue val = store.get(key);
        if (val == null || val.isExpired()) {
            store.remove(key);
            sendResponse(exchange, 200, "key not found or expired");
            return;
        }
        sendResponse(exchange, 200, val.data.toString());
    }

    private void handlePut(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        String key = query != null ? query.split("=")[1] : null;
        if (key == null)
            sendResponse(exchange, 400, "bad request/ missing id");
        InputStream is = exchange.getRequestBody();
        String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        store.put(key, new CacheValue(body, System.currentTimeMillis() + ttl));
        sendResponse(exchange, 201, "stored with id = " + key);
    }

    private void sendResponse(HttpExchange exchange, int status, String response) throws IOException {
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(status, bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }
}
