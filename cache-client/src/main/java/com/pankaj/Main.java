package com.pankaj;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        List<String> urls = List.of("http://localhost:8080");
        DistributedCacheClient client = new DistributedCacheClient(urls);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Cache4j: Distributed cache for java");
        System.out.println("Commands: PUT key value | GET key | EXIT");
        while(true){
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) continue;
            String[] parts = input.split(" ");
            String command = parts[0].toUpperCase();
            switch(command){
                case "PUT" -> {
                    if(parts.length < 3){
                        System.out.println("Usage: PUT key value");
                    }else{
                        String key = parts[1];
                        String value = input.substring(input.indexOf(key) + key.length()).trim();
                        client.put(key, value);
                    }
                }
                case "GET" -> {
                    if(parts.length < 2){
                        System.out.println("Usage: GET key");
                    }else{
                        System.out.println(client.get(parts[1]));
                    }
                }
                case "EXIT" -> {
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                }
                
                default -> System.out.println("Unknown command");
            }
        }
    }
}