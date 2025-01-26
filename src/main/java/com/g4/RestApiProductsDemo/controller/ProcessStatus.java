package com.g4.RestApiProductsDemo.controller;

public class ProcessStatus {
    private String message;
    private String threadName;
    private long threadId;

    public ProcessStatus(String message, String threadName, long threadId) {
        this.message = message;
        this.threadName = threadName;
        this.threadId = threadId;
    }

    // Getters and setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public long getThreadId() {
        return threadId;
    }

    public void setThreadId(long threadId) {
        this.threadId = threadId;
    }
}