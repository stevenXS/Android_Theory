package com.steven.Utils.thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class PriorityThreadPool extends ThreadPoolExecutor {
    private PriorityTaskQueue taskQueue;
    public PriorityThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        this.taskQueue = (PriorityTaskQueue) workQueue;
    }

    
}
