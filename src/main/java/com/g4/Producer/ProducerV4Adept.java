package com.g4.Producer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ProducerV4Adept {
    public static void main(String[] args) {
        for (int i = 1; i <= 10; i++) {
            Thread thread = new Thread(new V4AdeptCaller(i));
            thread.start();
        }
    }
}

class V4AdeptCaller implements Runnable {

    private final int threadNum;

    public V4AdeptCaller(int threadNum) {
        this.threadNum = threadNum;
    }

    @Override
    public void run() {
        for (int i = 0; i <= 20; i++) {
            try {
                URL url = new URL("http://localhost:8080/api/v4/product/async-adept");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();

                String response = content.toString();
                System.out.println(Thread.currentThread().getName() + " - Response: " + response);
                conn.disconnect();

            } catch (Exception e) {
                System.err.println(Thread.currentThread().getName() + " - Error: " + e.getMessage());
            }
        }
    }
}
