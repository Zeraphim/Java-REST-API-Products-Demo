package com.g4.Producer;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;

// Creates 10 threads that each loops initiate POST request to V3 API 20 times creating a product.

public class ProducerV3 {
    public static void main(String[] args) {
        for (int i = 1; i <= 10; i++) {
            Thread thread = new Thread(new ApiCaller(i));
            thread.start();
        }
    }
}

class ApiCaller implements Runnable {

    private final int threadNum;

    public ApiCaller(int threadNum) {
        this.threadNum = threadNum;
    }

    @Override
    public void run() {
        for (int i = 0; i <= 20; i++) {
            try {
                URL url = new URL("http://localhost:8080/api/v3/product");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);



                ArrayList<String> nameList = new ArrayList<>(Arrays.asList("Alienware", "Macbook Pro", "Macbook Air", "Steam Deck", "Nintendo Switch"));

                ArrayList<String> descriptionList = new ArrayList<>(Arrays.asList("Gaming Laptop", "It just works.", "It kinda works.", "Play anywhere", "Play with friends."));

                ArrayList<Integer> priceList = new ArrayList<>(Arrays.asList(72000,200000,60000,50000,30000));

                String jsonInputString = String.format("{\"name\": \"%s\", \"description\": \"%s\", \"price\": %d}", nameList.get(threadNum % 5), descriptionList.get(threadNum % 5), priceList.get(threadNum % 5));


                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                int responseCode = conn.getResponseCode();
                System.out.println(Thread.currentThread().getName() + " - Response Code: " + responseCode);
                conn.disconnect();
            } catch (Exception e) {
                System.err.println(Thread.currentThread().getName() + " - Error: " + e.getMessage());
            }
        }
    }
}