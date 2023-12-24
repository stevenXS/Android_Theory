package com.steven.Utils.thread;
import java.util.Comparator;
import java.util.concurrent.*;

public class PriorityTaskQueue extends PriorityBlockingQueue<Runnable> {
    @Override
    public Comparator<? super Runnable> comparator() {
        return new MyComparator();
    }

    public static class MyComparator implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {
            return ((Task) o1).getPriority() - ((Task) o2).getPriority();
        }
    }
}