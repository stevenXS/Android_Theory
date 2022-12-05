package com.steven.custom_view_demo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CustomTextView extends androidx.appcompat.widget.AppCompatTextView {
    private int mViewWidth = 0;
    private int mTranslate = 0;
    private Matrix mGradientMatrix;
    private LinearGradient mLinearGradient;

    public CustomTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        /**
         * 注：
         * -先调用父类再自定义绘制：会覆盖父类的设置，例如父类设置了文本。则此时会覆盖文本；
         * -先自定义绘制再调用父类，会保留父类的属性。
         */
        super.onDraw(canvas);
        // 实现闪动的文字效果
        if (mGradientMatrix == null){
            mTranslate += mViewWidth/5;
            if (mTranslate > 2*mViewWidth){
                mTranslate = -mViewWidth;
            }
            mGradientMatrix.setTranslate(mTranslate, 0);
            mLinearGradient.setLocalMatrix(mGradientMatrix);
            postInvalidateDelayed(100);
        }

//        // 设置画笔
//        Paint paint1 = new Paint();
//        paint1.setColor(getResources().getColor(R.color.purple_500));
//        paint1.setStyle(Paint.Style.FILL);
//        Paint paint2 = new Paint();
//        paint2.setColor(Color.YELLOW);
//        paint2.setStyle(Paint.Style.FILL);
//        // 绘制
//        canvas.drawRect(
//                0,
//                0,
//                getMeasuredWidth(),
//                getMeasuredHeight(),
//                paint1
//        );
//        canvas.drawRect(
//                10,
//                10,
//                getMeasuredWidth() - 10,
//                getMeasuredHeight() - 10,
//                paint2
//        );
//        canvas.save(); // 保存数据
//        // 绘制文字前平移10像素
//        canvas.translate(10,0);
//        super.onDraw(canvas);
//        canvas.restore(); // 清除当前save的数据
    }

    /**
     * 当View大小改变时的回调
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mViewWidth == 0){
            mViewWidth = getMeasuredWidth();
            if (mViewWidth > 0){
                Log.d("TV", String.valueOf(mViewWidth));
                TextPaint paint = getPaint();
                mLinearGradient = new LinearGradient(
                        0,
                        0,
                        mViewWidth,
                        0,
                        new int[]{
                                Color.BLUE, 0xffffff,
                                Color.BLUE},
                        null,
                        Shader.TileMode.CLAMP
                );
                paint.setShader(mLinearGradient);
                mGradientMatrix = new Matrix();
            }
        }
    }
}
