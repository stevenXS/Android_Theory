package com.steven.aidl_demo.test;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class AIDLTestService extends Service {
    private final IBinder binder = new IAIDLService.Stub() {

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public void psotMessage(IAIDLTestCallback callback) throws RemoteException {
            Log.d(AIDLClientActivity.TAG, "[before] callback");
            callback.handleMessage("from service: hello client");
            Log.d(AIDLClientActivity.TAG, "[after] callback");
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.d(AIDLClientActivity.TAG, "get package name from intent " + intent.getPackage() + ", bind binder.");
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(AIDLClientActivity.TAG, "onStartCommand: ");
        return super.onStartCommand(intent, flags, startId);
    }
}