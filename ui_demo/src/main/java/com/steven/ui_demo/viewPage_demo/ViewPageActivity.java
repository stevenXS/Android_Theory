package com.steven.ui_demo.viewPage_demo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.steven.ui_demo.R;

import java.util.ArrayList;
import java.util.List;

public class ViewPageActivity extends AppCompatActivity {
    RadioGroup rg;
    View view;
    ViewPager vp;
    int width;
    int currentPos;

    List<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_page);
        rg = (RadioGroup) findViewById(R.id.rg_home);
        vp = (ViewPager) findViewById(R.id.view_page);
        view = (View) findViewById(R.id.home_fl_view);

        fragmentList.add(new LeftFragment());
        fragmentList.add(new RightFragment());

        FragmentStatePagerAdapter adapter = new MyAdapter(getSupportFragmentManager());
        vp.setAdapter(adapter);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //点击后选择对应的ViewPager页面
                vp.setCurrentItem(checkedId == R.id.rg_btn1 ? 0 : 1);
            }
        });

        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            // 屏幕滑动的回调方法，设置指示器
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) view.getLayoutParams();
                //设置下划线距离左边的位置长度
                int left = (int) ((positionOffset + position) * width);
                lp.setMargins(left,0,0,0);
                view.setLayoutParams(lp);
                Log.d("onPageScrolled", "aaa");
                vp.setCurrentItem(1, false);
            }

            //屏幕被选择的回调方法
            @Override
            public void onPageSelected(int position) {
                rg.check(position == 0 ? R.id.rg_btn1: R.id.rg_btn2);
                Log.d("onPageSelected", "aaa");
            }

            //滑动页面状态的改变，这个方法不用理会
            //如果要实现自动轮播可以重写
            @Override
            public void onPageScrollStateChanged(int state) {

                Log.d("onPageScrollStateChanged", "state: "+ String.valueOf(state));

            }
        });


        // 初始化下划线
        width = getResources().getDisplayMetrics().widthPixels / 2;
        // 设置下划线View的长度
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(layoutParams);
    }

    class MyAdapter extends FragmentStatePagerAdapter{

        public MyAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            currentPos = position;
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }
}
