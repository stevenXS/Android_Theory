package com.steven.camera.session;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.os.Handler;

import androidx.annotation.NonNull;

/*封装相机相关的会话操作*/
@TargetApi(21)
public class PhotoSession extends Session {
    private final String TAG = "PreviewSession";

    /*相机状态常量*/
    private int mState = STATE_PREVIEW; // 相机当前状态
    private static final int STATE_PREVIEW = 0; // 相机预览
    private static final int STATE_WAITING_LOCK = 1; // 等待焦点锁定
    private static final int STATE_WAITING_PRECAPTURE = 2; // 等待曝光进入预捕捉状态
    private static final int STATE_WAITING_NON_PRECAPTURE = 3; // 等待曝光状态变为预捕获以外的状态
    private static final int STATE_PICTURE_TAKEN = 4; // 图片被拍摄

    // 相机捕捉状态回调
    final CameraCaptureSession.CaptureCallback mCaptureCallback = new CameraCaptureSession.CaptureCallback() {
        /*根据不同状态处理会话*/
        private void process(CaptureResult result) {
            switch (mState) {
                case STATE_PREVIEW:
                    break;
                case STATE_WAITING_LOCK: {
                    Integer afState = result.get(CaptureResult.CONTROL_AF_STATE);
                    if (afState == null) {
                        captureStillPicture();
                    } else if (CaptureResult.CONTROL_AF_STATE_FOCUSED_LOCKED == afState || CaptureResult.CONTROL_AF_STATE_NOT_FOCUSED_LOCKED == afState) {
                        Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                        if (aeState == null || aeState == CaptureResult.CONTROL_AE_STATE_CONVERGED) {
                            mState = STATE_PICTURE_TAKEN;
                            captureStillPicture();
                        } else {
                            runPreCaptureSequence(mBackgroundHandler);
                        }
                    }
                    break;
                }
                case STATE_WAITING_PRECAPTURE:{
                    Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                    if (aeState == null || aeState == CaptureResult.CONTROL_AE_STATE_PRECAPTURE ||
                            aeState == CaptureRequest.CONTROL_AE_STATE_FLASH_REQUIRED) {
                        mState = STATE_WAITING_NON_PRECAPTURE;
                    }
                    break;
                }
                case STATE_WAITING_NON_PRECAPTURE: {
                    // CONTROL_AE_STATE can be null on some devices
                    Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                    if (aeState == null || aeState != CaptureResult.CONTROL_AE_STATE_PRECAPTURE) {
                        mState = STATE_PICTURE_TAKEN;
                        captureStillPicture();
                    }
                    break;
                }
            }
        }

        @Override
        public void onCaptureProgressed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureResult partialResult) {
            process(partialResult);
        }

        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
            process(result);
        }

        @Override
        public void onCaptureFailed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureFailure failure) {
            // process error.
        }
    };

    public PhotoSession(Context context, CameraDeviceManager manager, CameraCharacteristics characteristics) {
        super(context, manager, characteristics);
    }

    @Override
    public void postRequest(int type, SurfaceTexture surfaceTexture) {
        switch (type) {
            case RQ_START_PREVIEW:
                createCameraPreviewSession(surfaceTexture, null, mCaptureCallback, null);
                break;
            case RQ_TAKE_PHOTO:
                takePicture();
                break;
            case RQ_RELEASE:
                release();
                break;
            case RQ_STOP_PREVIEW:
                stopPreviewRequest(mCaptureSession, mPreviewRequestBuilder.build(), mCaptureCallback,null);
                break;
            default:
                break;
        }
    }

    public void takePicture() {
        lockFoucus();
    }

    private void lockFoucus() {
        try {
            // This is how to tell the camera to lock focus.
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AF_TRIGGER_START);
            // Tell #mCaptureCallback to wait for the lock.
            mState = STATE_WAITING_LOCK;
            // capture：用于单拍请求
            mCaptureSession.capture(mPreviewRequestBuilder.build(), mCaptureCallback, mBackgroundHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void unlockFocus() {
        try {
            // Reset the auto-focus trigger
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER,
                    CameraMetadata.CONTROL_AF_TRIGGER_CANCEL);
            mCameraSettings.setAutoFlash(mPreviewRequestBuilder);
            mCaptureSession.capture(mPreviewRequestBuilder.build(), mCaptureCallback,
                    mBackgroundHandler);
            // After this, the camera will go back to the normal state of preview.
            mState = STATE_PREVIEW;
            mCaptureSession.setRepeatingRequest(mPreviewRequest, mCaptureCallback,
                    mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 捕捉静态图像的请求
     */
    private void captureStillPicture() {
        final CameraCaptureSession.CaptureCallback captureCallback = new CameraCaptureSession.CaptureCallback() {
            @Override
            public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                unlockFocus();
            }
        };
        int rotation = ((Activity) mContext).getWindowManager().getDefaultDisplay().getRotation();
        final CaptureRequest.Builder builder = createBuilder(CameraDevice.TEMPLATE_STILL_CAPTURE, mImageReader.getSurface());
        builder.set(CaptureRequest.JPEG_ORIENTATION, mCameraSettings.getOrientation(rotation));
        mCameraSettings.setAutoFlash(builder);
        stopPreviewRequest(mCaptureSession, builder.build(), captureCallback, null);
    }

    private void runPreCaptureSequence(Handler handler) {
        try {
            // 告诉相机触发方式
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER, CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER_START);
            // 通知回调#mCaptureCallback等待precapture sequence被设置
            mState = STATE_WAITING_PRECAPTURE;
            mPreviewSession.capture(mPreviewRequestBuilder.build(), mCaptureCallback, handler);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
