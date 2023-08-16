package com.steven.camera.session;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.RectF;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Display;
import android.view.Surface;
import android.view.TextureView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@TargetApi(21)
public class CameraSettings {
    private final String TAG = "CameraSettings";
    private final CameraCharacteristics mCharacteristics;
    private Context mContext;

    /*Camera2API 支持的的最大预览宽度*/
    private static final int MAX_PREVIEW_WIDTH = 1920;

    /*Camera2API 支持的的最大预览高度*/
    private static final int MAX_PREVIEW_HEIGHT = 1080;

    private int mSensorOrientation;

    /*屏幕旋转角度转化为JPEG旋转角度*/
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    /*自定义比较器类*/
    static class CompareSizesByArea implements Comparator<Size> {
        @Override
        public int compare(Size lhs, Size rhs) {
            // signum：提取实数符号的函数，输入值小于0则返回-1；大于0则返回1
            return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                    (long) rhs.getWidth() * rhs.getHeight());
        }
    }

    public CameraSettings(Context context, CameraCharacteristics cameraCharacteristics) {
        mCharacteristics = cameraCharacteristics;
        mContext = context;
    }

    public int getOrientation(int rotation) {
        // Sensor orientation is 90 for most devices, or 270 for some devices (eg. Nexus 5X)
        // We have to take that into account and rotate JPEG properly.
        // For devices with orientation of 90, we simply return our mapping from ORIENTATIONS.
        // For devices with orientation of 270, we need to rotate the JPEG 180 degrees.
        return (ORIENTATIONS.get(rotation) + mSensorOrientation + 270) % 360;
    }

    /*
     * 获取Surface宽、高，以及限制相机支持的最大宽、高，并判断缩放比是否与获取的尺寸匹配；
     * 若不匹配这类尺寸，则选择与指定的比例最大的尺寸。
     * */
    public Size chooseOptimalSize(Size[] choices, int textureViewWidth, int textureViewHeight,
                                  int maxWidth, int maxHeight, Size aspectRatio) {
        // 收集与Surface一样大小的且当前相机支持的分辨率
        List<Size> bigEnough = new ArrayList<>();
        // 收集比Surface尺寸小且当前相机支持的分辨率
        List<Size> notBigEnough = new ArrayList<>();
        int w = aspectRatio.getWidth();
        int h = aspectRatio.getHeight();
        for (Size option : choices) {
            if (option.getWidth() <= maxWidth && option.getHeight() <= maxHeight &&
                    option.getHeight() == option.getWidth() * h / w) {
                if (option.getWidth() >= textureViewWidth &&
                        option.getHeight() >= textureViewHeight) {
                    bigEnough.add(option);
                } else {
                    notBigEnough.add(option);
                }
            }
        }
        if (bigEnough.size() >0) {
            return Collections.min(bigEnough, new CompareSizesByArea());
        } else if (notBigEnough.size() > 0) {
            return Collections.max(notBigEnough, new CompareSizesByArea());
        } else {
            return choices[0];
        }
    }

    /**
     * 根据当前相机属性返回支持的分辨率列表
     * SCALER_STREAM_CONFIGURATION_MAP：以长边为宽，短边为高（竖屏&横屏皆如此）
     */
    public StreamConfigurationMap getConfigMap() {
        return mCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
    }

    /**
     * 当前设备是否支持Flash
     */
    public boolean isSupportFlash() {
        Boolean support = mCharacteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
        return support != null && support;
    }

    /**
     * 设置自动闪光
     */
    public void setAutoFlash(CaptureRequest.Builder builder) {
        if (builder == null) {
            return;
        }
        if (isSupportFlash()) {
            builder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
        }
    }

    /**
     *
     * @param map 设备支持的分辨率列表
     * @param format {@link ImageFormat#JPEG}
     * @return 根据输入格式返回默认的最大图片尺寸
     */
    public Size getDefaultLargestPictureSize(StreamConfigurationMap map, int format) {
        Size[] outputSizes = map.getOutputSizes(format);
        return Collections.max(Arrays.asList(outputSizes), new CompareSizesByArea());
    }

    /**
     *
     * @param map 设备支持的分辨率列表
     * @param cls SurfaceView.class
     * @return 根据输入的View返回默认最大预览尺寸
     */
    public Size getDefaultLargestPreviewSize(StreamConfigurationMap map, Class<Object> cls) {
        Size[] outputSizes = map.getOutputSizes(cls);
        return Collections.max(Arrays.asList(outputSizes), new CompareSizesByArea());
    }

    /**
     * 选择最合适预览尺寸
     */
    public Size chooseOptimalPreviewSize(Context context, StreamConfigurationMap map) {
        if (context == null) {
            return null;
        }
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        int rotation = display.getRotation();
        mSensorOrientation = mCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
        boolean swappedDimensions = false;
        switch (rotation) {
            case Surface.ROTATION_0:
            case Surface.ROTATION_180:
                if (mSensorOrientation == 90 || mSensorOrientation == 270) {
                    swappedDimensions = true;
                }
                break;
            case Surface.ROTATION_90:
            case Surface.ROTATION_270:
                if (mSensorOrientation == 0 || mSensorOrientation == 180) {
                    swappedDimensions = true;
                }
                break;
            default:
                Log.e(TAG, "Display rotation is invalid: " + rotation);
        }
        Point displaySize = new Point();
        display.getSize(displaySize);
        final Size pictureSize = getDefaultLargestPictureSize(map, ImageFormat.JPEG);
        int rotatedPreviewWidth = pictureSize.getWidth();
        int rotatedPreviewHeight = pictureSize.getHeight();
        int maxPreviewWidth = displaySize.x;
        int maxPreviewHeight = displaySize.y;
        if (swappedDimensions) {
            rotatedPreviewWidth = pictureSize.getHeight();
            rotatedPreviewHeight = pictureSize.getWidth();
            maxPreviewWidth = displaySize.y;
            maxPreviewHeight = displaySize.x;
        }
        if (maxPreviewWidth > MAX_PREVIEW_WIDTH) {
            maxPreviewWidth = MAX_PREVIEW_WIDTH;
        }
        if (maxPreviewHeight > MAX_PREVIEW_HEIGHT) {
            maxPreviewHeight = MAX_PREVIEW_HEIGHT;
        }
        Size largest = Collections.max(Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)), new CompareSizesByArea());
        return chooseOptimalSize(map.getOutputSizes(ImageFormat.JPEG),
                rotatedPreviewWidth, rotatedPreviewHeight,
                maxPreviewWidth, maxPreviewHeight, largest);
    }

    /**
     *
     * @param viewWidth the width of 'TextureView/SurfaceTexture'
     * @param viewHeight the height of 'TextureView/SurfaceTexture'
     * @param previewSize 当前相机的预览尺寸
     */
    public void configTransform(int viewWidth, int viewHeight, Size previewSize, TextureView textureView) {
        if (previewSize == null || textureView == null || mContext == null) {
            return;
        }
        final int rotation = ((Activity) mContext).getWindowManager().getDefaultDisplay().getRotation();
        Matrix matrix = new Matrix();
        RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
        RectF bufferRect = new RectF(0, 0, previewSize.getHeight(), previewSize.getWidth());
        float centerX = viewRect.centerX();
        float centerY = viewRect.centerY();
        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
            float scale = Math.max(
                    (float) viewHeight / previewSize.getHeight(),
                    (float) viewWidth / previewSize.getWidth());
            matrix.postScale(scale, scale, centerX, centerY);
            matrix.postRotate(90 * (rotation - 2), centerX, centerY);
        } else if (Surface.ROTATION_180 == rotation) {
            matrix.postRotate(180, centerX, centerY);
        }
        textureView.setTransform(matrix);
    }
}