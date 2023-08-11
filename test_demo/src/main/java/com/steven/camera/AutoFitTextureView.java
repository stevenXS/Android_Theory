package com.steven.camera;

import android.content.Context;
import android.util.AttributeSet;
import android.view.TextureView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/*播放视频或渲染动画的View，与之对应的View还有SurfaceView*/
public class AutoFitTextureView extends TextureView {
    private int mRatioWidth = 0;
    private int mRatioHeight = 0;

    public AutoFitTextureView(@NonNull Context context) {
        super(context);
    }

    public AutoFitTextureView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoFitTextureView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 设置此视图的纵横比。视图的大小将根据比例进行测量,根据参数计算。请注意，参数的实际大小并不重要
     * 即，调用setAspectRatio（2，3）和setAspectRatio（4，6）会得到相同的结果。
     * @param width
     * @param height
     */
    public void setAspectRation(int width, int height) {
        if (width <0 || height <0) {
            throw new IllegalArgumentException("Size cannot be negative.");
        }
        mRatioHeight = width;
        mRatioHeight = height;
        super.requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (0 == mRatioWidth || 0 == mRatioHeight) {
            setMeasuredDimension(width, height);
        } else {
            // 根据新的纵横比跟新W/H
            if (width < height * mRatioWidth / mRatioHeight) {
                setMeasuredDimension(width, width * mRatioHeight / mRatioWidth);
            } else {
                setMeasuredDimension(height * mRatioWidth / mRatioHeight, height);
            }
        }
    }
}
