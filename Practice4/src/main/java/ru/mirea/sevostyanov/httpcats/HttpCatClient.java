package ru.mirea.sevostyanov.httpcats;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class HttpCatClient {
    public static void main(String[] args) throws IOException, InterruptedException {
        File dir = new File("cats");
        if (!dir.exists()) {
            dir.mkdir();
        }

        HttpClient client = HttpClient.newHttpClient();
        ArrayList<CompletableFuture<Void>> futures = new ArrayList<>();

        for (int i = 0; i <= 800; i += 100) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://http.cat/" + i))
                    .GET()
                    .build();

            System.out.println("Send request to https://http.cat/" + i);

            int finalI = i;
            CompletableFuture<Void> ft = client.sendAsync(request, HttpResponse.BodyHandlers.ofByteArray())
                    .thenApply(HttpResponse::body)
                    .thenAccept(body -> saveToFile(body, finalI))
                    .exceptionally(throwable -> {
                        System.err.println("Error processing request " + finalI + ": " + throwable.getMessage());
                        return null;
                    });

            futures.add(ft);
            Thread.sleep(100);
        }

        System.out.println("Wait for end of jobs");
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        System.out.println("All downloads completed");
    }

    private static void saveToFile(byte[] body, int i) {
        if (body == null || body.length == 0) {
            System.out.println("Empty response for cat " + i);
            return;
        }

        File outputFile = new File("cats/cat_" + i + ".jpg");
        try (FileOutputStream fos = new FileOutputStream(outputFile);
             BufferedOutputStream outputStream = new BufferedOutputStream(fos)) {

            outputStream.write(body);
            outputStream.flush();
            System.out.println("Successfully saved cat " + i);

        } catch (IOException e) {
            System.err.println("Error saving cat " + i + ": " + e.getMessage());
        }
    }
}