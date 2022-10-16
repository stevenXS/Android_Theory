package com.steven.async_demo.async;

public class MyFutureTask {
    private AsyncTask asyncTask = null;
    private boolean isReady = false;

    public synchronized void setData(AsyncTask asyncTask){
        if (isReady){
            return;
        }
        this.asyncTask = asyncTask;
        isReady = true;
        notifyAll();
    }

    public synchronized String getResult(){
        while (!isReady){
            try {
                wait();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        return asyncTask.result();
    }
}
