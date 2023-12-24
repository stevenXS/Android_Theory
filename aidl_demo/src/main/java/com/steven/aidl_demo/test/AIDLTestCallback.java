package com.steven.aidl_demo.test;

import android.os.RemoteException;
import android.util.Log;

public class AIDLTestCallback extends IAIDLTestCallback.Stub {
    @Override
    public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

    }

    @Override
    public void handleMessage(String msg) throws RemoteException {
        Log.d(AIDLClientActivity.TAG, "get msg " + msg);
    }
}
