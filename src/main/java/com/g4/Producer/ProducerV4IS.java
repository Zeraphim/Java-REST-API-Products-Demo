package com.g4.Producer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ProducerV4IS {
    public static void main(String[] args) {
        for (int i = 1; i <= 10; i++) {
            Thread thread = new Thread(new V4ISCaller(i));
            thread.start();
        }
    }
}

class V4ISCaller implements Runnable {

    private final int threadNum;

    public V4ISCaller(int threadNum) {
        this.threadNum = threadNum;
    }

    @Override
    public void run() {
        for (int i = 0; i <= 20; i++) {
            try {
                URL url = new URL("http://localhost:8080/api/v4/product/asyncEvent");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                boolean processStarted = false;

                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                    if (inputLine.contains("\"message\":\"Process started\"")) {
                        System.out.println(Thread.currentThread().getName() + " - Response: " + inputLine);
                        processStarted = true;
                    } else if (processStarted && inputLine.contains("\"message\":\"Process completed\"")) {
                        System.out.println(Thread.currentThread().getName() + " - Response: " + inputLine);
                        break;
                    }
                }
                in.close();
                conn.disconnect();

            } catch (Exception e) {
                System.err.println(Thread.currentThread().getName() + " - Error: " + e.getMessage());
            }
        }
    }
}