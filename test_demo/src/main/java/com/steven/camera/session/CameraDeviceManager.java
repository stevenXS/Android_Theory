package com.steven.camera.session;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@TargetApi(21)
public class CameraDeviceManager {
    private final String TAG = "CameraDeviceManager";
    private static final int RQ_START_PREVIEW = 10;
    private final Context mContext;

    /*相机管理类*/
    private final CameraManager mCameraManager;

    /*相机实例*/
    private CameraDevice mCameraDevice;

    /*控制相机开关的锁*/
    private final Semaphore mCameraOpenCloseLock = new Semaphore(1);

    /*相机ID，默认前置1*/
    protected String mCameraId = "1";

    protected CameraEvent mCameraEvent;

    public CameraDeviceManager(Context context, CameraEvent event) {
        if (context == null) {
            throw new RuntimeException("context is null");
        }
        mCameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        mContext = context;
        mCameraEvent = event;
    }

    public CameraManager getCameraManager() {
        return mCameraManager;
    }

    public CameraDevice getCameraDevice() {
        return mCameraDevice;
    }

    public String getCameraId() {
        return mCameraId;
    }

    public void setCameraId(String mCameraId) {
        this.mCameraId = mCameraId;
    }

    /**
     * @param cameraId The unique identifier of the camera device to open
     * @param callback The callback which is invoked once the camera is opened
     * @param handler The handler on which the callback should be invoked, or null to use the current thread's looper.
     */
    @SuppressLint("MissingPermission")
    public void openCamera(int cameraId, CameraDevice.StateCallback callback, Handler handler) {
        mCameraId = String.valueOf(cameraId);
        try {
            /*refer google official demo*/
            if (!mCameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
                throw new RuntimeException("Time out waiting to lock camera opening.");
            }
            mCameraManager.openCamera(mCameraId, callback, handler);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openCamera(int cameraId) {
        mCameraId = String.valueOf(cameraId);
        /*相机初始化回调*/
        final CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {
            @Override
            public void onOpened(@NonNull CameraDevice camera) {
                mCameraOpenCloseLock.release();
                mCameraDevice = camera;
                mCameraEvent.onCameraOpened(camera);
            }

            @Override
            public void onDisconnected(@NonNull CameraDevice camera) {
                mCameraOpenCloseLock.release();
                camera.close();
                mCameraDevice = null;
            }

            @Override
            public void onError(@NonNull CameraDevice camera, int error) {
                mCameraOpenCloseLock.release();
                camera.close();
                mCameraDevice = null;
            }
        };
        openCamera(cameraId, mStateCallback, null);
    }

    public void release() {
        if (mCameraDevice != null) {
            mCameraDevice.close();
            mCameraDevice = null;
        }
    }

    public String[] getCameraIdList() {
        try {
            return mCameraManager.getCameraIdList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public CameraSettings getCameraSettings(int cameraId) {
        CameraCharacteristics cameraCharacteristics;
        try {
            cameraCharacteristics = mCameraManager.getCameraCharacteristics(String.valueOf(cameraId));
        } catch (CameraAccessException e) {
            throw new RuntimeException(e);
        }
        return new CameraSettings(mContext, cameraCharacteristics);
    }

    public static abstract class CameraEvent {
        public void onCameraOpened(CameraDevice device) {
        }
    }
}
