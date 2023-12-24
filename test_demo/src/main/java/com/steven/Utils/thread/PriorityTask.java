package com.steven.Utils.thread;

import android.util.Log;

import java.util.concurrent.TimeUnit;

public class PriorityTask implements Runnable, Comparable<PriorityTask>{
    private final String name;
    private final Integer priority;

    public PriorityTask(String name, int priority) {
        this.name = name;
        this.priority = priority;
    }

    @Override
    public int compareTo(PriorityTask o) {
        return Integer.compare(o.priority, this.priority);
    }

    @Override
    public void run() {
        Log.d("aaa", name + ", " + priority);
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
