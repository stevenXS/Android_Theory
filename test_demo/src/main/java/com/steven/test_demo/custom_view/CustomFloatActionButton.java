package com.steven.test_demo.custom_view;

import android.animation.ObjectAnimator;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.steven.test_demo.R;

public class CustomFloatActionButton extends FloatingActionButton {
    private int parentHeight;
    private int parentWidth;
    private int lastX;
    private int lastY;
    private boolean isDrag;

    public CustomFloatActionButton(Context context) {
        super(context);
    }

    public CustomFloatActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomFloatActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(true, left, top, right, bottom);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int rawX = (int) event.getRawX();
        int rawY = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                setPressed(true);
                isDrag=false;
                getParent().requestDisallowInterceptTouchEvent(true);
                lastX=rawX;
                lastY=rawY;
                ViewGroup parent;
                if(getParent()!=null){
                    parent= (ViewGroup) getParent();
                    parentHeight=parent.getHeight();
                    parentWidth=parent.getWidth();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(parentHeight<=0||parentWidth==0){
                    isDrag=false;
                    break;
                }else {
                    isDrag=true;
                }
                //计算手指移动了多少
                int dx=rawX-lastX;
                int dy=rawY-lastY;
                //这里修复一些华为手机无法触发点击事件
                int distance= (int) Math.sqrt(dx*dx+dy*dy);
                if(distance==0){
                    isDrag=false;
                    break;
                }
                float x=getX()+dx;
                float y=getY()+dy;
                //检测是否到达边缘 左上右下
                x=x<0?0:x>parentWidth-getWidth()?parentWidth-getWidth():x;
                y=getY()<0?0:getY()+getHeight()>parentHeight?parentHeight-getHeight():y;
                setX(x);
                setY(y);
                lastX=rawX;
                lastY=rawY;
                break;
            case MotionEvent.ACTION_UP:
                if(!isNotDrag()){
                    //恢复按压效果
                    setPressed(false);
                    //Log.i("getX="+getX()+"；screenWidthHalf="+screenWidthHalf);
               /* animate().setInterpolator(new DecelerateInterpolator())
                        .setDuration(500)
                        .start();*/
                    if(rawX>=parentWidth/2){
                        //靠右吸附
                        animate().setInterpolator(new DecelerateInterpolator())
                                .setDuration(500)
                                .xBy(parentWidth-getWidth()-getX())
                                .start();
                    }else {
                        //靠左吸附
                        ObjectAnimator oa=ObjectAnimator.ofFloat(this,"x",getX(),0);
                        oa.setInterpolator(new DecelerateInterpolator());
                        oa.setDuration(500);
                        oa.start();
                    }
                }
                break;
        }
        return !isNotDrag() || super.onTouchEvent(event);
    }

    private boolean isNotDrag(){
        return !isDrag&&(getX()==0
                ||(getX()==parentWidth-getWidth()));
    }

    public static class FloatButtonListener implements View.OnClickListener {
        private Context context;
        private String message;

        public FloatButtonListener(Context c, String msg) {
            context = c;
            message = msg;
        }

        @Override
        public void onClick(View v) {
            Log.d("aaa", "aaaaaaaaaaaaaaa");
            showPopupMenu(v);
        }

        /** 显示菜单 */
        private void showPopupMenu(final View view) {
            PopupMenu popupMenu = new PopupMenu(context, view);
            popupMenu.getMenuInflater().inflate(R.menu.menu_float_button, popupMenu.getMenu()); // 初始化菜单布局
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Toast.makeText(context, "复制成功", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
            popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
                @Override
                public void onDismiss(PopupMenu menu) {
                    Toast.makeText(context, "取消", Toast.LENGTH_SHORT).show();
                }
            });
            popupMenu.show();
        }
    }
}
