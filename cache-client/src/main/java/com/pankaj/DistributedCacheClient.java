package com.pankaj;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class DistributedCacheClient {
    private final ConsistentHashRing ring;
    private final HttpClient client;

    public DistributedCacheClient(List<String> nodes){
        this.ring = new ConsistentHashRing();
        nodes.forEach(ring::addNode);
        client = HttpClient.newHttpClient();
    }

    public void put(String key, String value) throws IOException, InterruptedException{
        String node = ring.getNode(key);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(node + "/cache?key="+node))
                .PUT(HttpRequest.BodyPublishers.ofString(value))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public String get(String key) throws IOException, InterruptedException{
        String node = ring.getNode(key);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(node + "/cache?key="+node))
                .GET()
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
    }

}
