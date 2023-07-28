package com.steven.test_demo.customSearchView.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.steven.test_demo.R;

/**
 * 自定义搜索框
 */
public class CustomEditText extends androidx.appcompat.widget.AppCompatEditText {
    /* 自定义左侧删除图标 */
    private Drawable clearDrawable;

    /* 自定义左侧搜索图标 */
    private Drawable searchDrawable;


    public CustomEditText(Context context) {
        super(context);
        init();
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        clearDrawable = getResources().getDrawable(R.drawable.clear);
        searchDrawable = getResources().getDrawable(R.drawable.search);

        // setCompoundDrawablesWithIntrinsicBounds(Drawable left, Drawable top, Drawable right, Drawable bottom)介绍
        // 作用：在EditText上、下、左、右设置图标（相当于android:drawableLeft=""  android:drawableRight=""）
        // 注1：setCompoundDrawablesWithIntrinsicBounds（）传入的Drawable的宽高=固有宽高（自动通过getIntrinsicWidth（）& getIntrinsicHeight（）获取）
        // 注2：若不想在某个地方显示，则设置为null

        // 另外一个相似的方法：setCompoundDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom)介绍
        // 与setCompoundDrawablesWithIntrinsicBounds（）的区别：可设置图标大小
        // 传入的Drawable对象必须已经setBounds(x,y,width,height)，即必须设置过初始位置、宽和高等信息
        // x:组件在容器X轴上的起点 y:组件在容器Y轴上的起点 width:组件的长度 height:组件的高度
        setCompoundDrawablesWithIntrinsicBounds(searchDrawable, null, null, null);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        // 获取到焦点且传入的字符不为空显示搜索图标
        setClearIconVisible(hasFocus() && text.length() > 0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                // 捕捉到手指抬起在删除图标区域内，等价于点击删除图标，此时清空数据
                // event.getX() ：抬起时的位置坐标
                // getWidth()：控件的宽度
                // getPaddingRight():删除图标图标右边缘至EditText控件右边缘的距离
                // 即：getWidth() - getPaddingRight() = 删除图标的右边缘坐标 = X1
                // getWidth() - getPaddingRight() - drawable.getBounds().width() = 删除图标左边缘的坐标 = X2
                // 所以X1与X2之间的区域 = 删除图标的区域
                // 当手指抬起的位置在删除图标的区域（X2=<event.getX() <=X1），即视为点击了删除图标 = 清空搜索框内容
                Drawable drawable = clearDrawable;
                if (drawable != null
                        && event.getX() <= (getWidth() - getPaddingRight())
                        && event.getX() >= (getWidth() - getPaddingRight() - drawable.getBounds().width())) {
                    setText("");
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private void setClearIconVisible(boolean b) {
        setCompoundDrawablesWithIntrinsicBounds(searchDrawable, null, b? clearDrawable : null, null);
    }


}
























