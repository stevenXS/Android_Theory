package com.steven.camera.session;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

/* 负责封装向Camera2发送各类请求的类 */
@TargetApi(21)
public abstract class Session {
    public static final int RQ_START_PREVIEW = 1;
    public static final int RQ_STOP_PREVIEW = 2;
    public static final int RQ_RELEASE = 3;
    public static final int RQ_TAKE_PHOTO = 4;

    protected Context mContext;

    /*相机配置类*/
    protected final CameraSettings mCameraSettings;

    /*相机会话类*/
    protected CameraCaptureSession mCaptureSession;

    /*相机配置项类*/
    protected CameraDeviceManager mCameraDeviceManager;

    /*预览会话*/
    protected CameraCaptureSession mPreviewSession;

    /*预览请求*/
    protected CaptureRequest.Builder mPreviewRequestBuilder;

    protected CaptureRequest mPreviewRequest;

    /*图像处理类*/
    protected ImageReader mImageReader;

    /*后台线程*/
    protected HandlerThread mBackgroundThread;

    /*处理后台任务handler*/
    protected Handler mBackgroundHandler;

    protected Size mPreviewSize;

    public abstract void postRequest(int type, TextureView surfaceTexture);

    protected Session(Context context, CameraDeviceManager manager, CameraCharacteristics characteristics) {
        mContext = context;
        mCameraDeviceManager = manager;
        mCameraSettings = new CameraSettings(context, characteristics);
    }

    /**
     * 调用时机：相机打开后的{@link CameraDevice.StateCallback#onOpened(CameraDevice)}回调中开启预览
     * @param textureView：支持视频的View
     * @param handler: null表示在当前线程处理，反之在handler对应线程处理
     */
    protected void createCameraPreviewSession(@NonNull TextureView textureView, @Nullable CaptureRequest.Builder builder,
                                              final CameraCaptureSession.CaptureCallback captureCallback, @Nullable final Handler handler) {
        final CameraDevice cameraDevice = mCameraDeviceManager.getCameraDevice();
        if (cameraDevice == null) {
            return;
        }
        // 开启后台线程
        startBackgroundThread();
        // 相机捕捉回调
        final CameraCaptureSession.StateCallback stateCallback = new CameraCaptureSession.StateCallback() {
            @Override
            public void onConfigured(@NonNull CameraCaptureSession session) {
                mPreviewSession = session;
                mCameraSettings.setAutoFlash(mPreviewRequestBuilder);
                mPreviewRequest = mPreviewRequestBuilder.build();
                mCameraSettings.setAutoFlash(mPreviewRequestBuilder);
                startPreviewRequest(mPreviewSession, mPreviewRequest, captureCallback, handler);
            }

            @Override
            public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                // 错误处理
            }

            @Override
            public void onClosed(@NonNull CameraCaptureSession session) {
                super.onClosed(session);
                release();
            }
        };
        // 创建预览会话
        SurfaceTexture surfaceTexture = textureView.getSurfaceTexture();
        final Surface surface = new Surface(surfaceTexture);
        List<Surface> outputSize = setUpOutputSize(mContext, surfaceTexture);
        mCameraSettings.configTransform(textureView.getWidth(), textureView.getHeight(), mPreviewSize, textureView);
        surfaceTexture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
        try {
            if (builder != null) {
                mPreviewRequestBuilder = builder;
            } else {
                mPreviewRequestBuilder = createBuilder(CameraDevice.TEMPLATE_PREVIEW, surface);
            }
            mPreviewRequestBuilder.addTarget(surface);
            cameraDevice.createCaptureSession(outputSize, stateCallback, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*为SurfaceTexture设置合适的预览尺寸*/
    private List<Surface> setUpOutputSize(Context context, SurfaceTexture surfaceTexture) {
        final ImageReader.OnImageAvailableListener mOnImageAvailableListener = new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader reader) {
                getByteFromImageReader(reader);
            }
        };
        final StreamConfigurationMap configMap = mCameraSettings.getConfigMap();
        final Size largest = mCameraSettings.getDefaultLargestPictureSize(configMap, ImageFormat.JPEG);
        mImageReader = ImageReader.newInstance(largest.getWidth(), largest.getHeight(), ImageFormat.JPEG, 2);
        mImageReader.setOnImageAvailableListener(mOnImageAvailableListener, new Handler(Looper.getMainLooper()));
        mPreviewSize = mCameraSettings.chooseOptimalPreviewSize(context, configMap);
        if (mPreviewSize != null) {
            surfaceTexture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
        }
        return Arrays.asList(new Surface(surfaceTexture), mImageReader.getSurface());
    }

    public Size getPreviewSize() {
        return mPreviewSize;
    }

    public CaptureRequest.Builder createBuilder(int type, Surface surface) {
        try {
            CameraDevice cameraDevice = mCameraDeviceManager.getCameraDevice();
            if (cameraDevice != null) {
                CaptureRequest.Builder builder = cameraDevice.createCaptureRequest(type);
                builder.addTarget(surface);
                return builder;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*获取图像元数据*/
    public byte[] getByteFromImageReader(ImageReader reader) {
        byte[] bytes = new byte[0];
        Image image = reader.acquireNextImage();
        try {
            if (image != null) {
                ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                bytes = new byte[buffer.remaining()];
                buffer.get(bytes); // buffer数据写入bytes数组
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (image != null) {
                try {
                    image.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return bytes;
    }


    /**
     * 重复请求图像，例如连拍、预览等场景
     * @param session
     * @param request
     * @param callback
     * @param handler
     */
    public void startPreviewRequest(CameraCaptureSession session, CaptureRequest request,
                                    CameraCaptureSession.CaptureCallback callback, @Nullable Handler handler) {
        if (request == null || callback == null) {
            return;
        }
        try {
            session.setRepeatingRequest(request, callback, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*拍照请求 handler = null，则在当前线程处理*/
    public void startCaptureRequest(CameraCaptureSession session, CaptureRequest request,
                                    CameraCaptureSession.CaptureCallback callback, @Nullable Handler handler) {
        if (request == null || callback == null || session == null) {
            return;
        }
        try {
            session.capture(request, callback, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*关闭预览请求，handler = null，则在当前线程处理*/
    public void stopPreviewRequest(CameraCaptureSession session, CaptureRequest request,
                                   CameraCaptureSession.CaptureCallback callback, @Nullable Handler handler) {
        if (request == null || callback == null || session == null) {
            return;
        }
        try {
            session.stopRepeating();
            session.abortCaptures();
            session.capture(request, callback, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("CameraBackground");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    public void release() {
        if (mBackgroundHandler != null) {
            mBackgroundThread.quitSafely();
            try {
                mBackgroundThread.join();
                mBackgroundThread = null;
                mBackgroundHandler = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (this.mPreviewSession != null) {
            this.mPreviewSession.close();
            this.mPreviewSession = null;
        }
        if (mImageReader != null) {
            mImageReader.close();
            mImageReader = null;
        }
    }
}