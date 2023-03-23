package com.steven.aidl_demo;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class BookManagerService extends Service {
    private static final String TAG = "BookManagerService";
    private CopyOnWriteArrayList<Book> bookList = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<IOnBookGetListener> listenersList = new CopyOnWriteArrayList<>();
    private AtomicBoolean isAlive = new AtomicBoolean(false);

    public BookManagerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG,"onCreate");

        bookList.add(new Book(1,"android"));
        bookList.add(new Book(2,"ios"));

        new Thread(new AddBookWorker()).start();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG,"onStartCommand start");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG,"onBind");
        return mBinder;
    }


    /**
     * 创建Binder对象
     */
    private Binder mBinder = new IBookManager.Stub(){
        @Override
        public List<Book> getBookList() throws RemoteException {
            return bookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            bookList.add(book);
        }

        @Override
        public void registerListener(IOnBookGetListener listener) throws RemoteException {
            listenersList.add(listener);
        }

        @Override
        public void unRegisterListener(IOnBookGetListener listener) throws RemoteException {
            if (listenersList.contains(listener))
                listenersList.remove(listener);
        }
    };

    class AddBookWorker extends Thread{
        @Override
        public void run() {
            while (!isAlive.get()){
                int i = bookList.size() + 1;
                Book new_book = new Book(i, "new book");
                try {
                    // 延迟处理，让客户端添加Listener
                    Thread.sleep(2000);
                    notifyAllListeners(new_book);
                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void notifyAllListeners(Book newBook) throws RemoteException {
        bookList.add(newBook);
        for (int i = 0; i < listenersList.size(); i++) {
            listenersList.get(i).onNewBookArrived(newBook);
        }
    }

    @Override
    public void onDestroy() {
        isAlive.set(true);
        super.onDestroy();
    }
}