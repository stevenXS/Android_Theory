package com.steven.Utils.thread;

public class Task implements Runnable {
    private int priority;
    private String taskName;

    public Task(int priority, String taskName) {
        this.priority = priority;
        this.taskName = taskName;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public void run() {
        System.out.println("Executing task: " + taskName);
    }
}