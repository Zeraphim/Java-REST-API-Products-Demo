package com.g4.Producer;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;

// Creates 10 threads that each loops initiate POST request to V3 API 20 times creating a product.

public class ProducerV4ForMQ {
    public static void main(String[] args) {
        for (int i = 1; i <= 10; i++) {
            Thread thread = new Thread(new V4MqApiCaller(i));
            thread.start();
        }
    }
}

class V4MqApiCaller implements Runnable {

    private final int threadNum;

    public V4MqApiCaller(int threadNum) {
        this.threadNum = threadNum;
    }

    @Override
    public void run() {
        for (int i = 0; i <= 20; i++) {
            try {
                URL url = new URL("http://localhost:8080/api/v4/product/async-advanced");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                int responseCode = conn.getResponseCode();
                System.out.println(Thread.currentThread().getName() + " - Response Code: " + responseCode);
                conn.disconnect();

            } catch (Exception e) {
                System.err.println(Thread.currentThread().getName() + " - Error: " + e.getMessage());
            }
        }
    }
}
