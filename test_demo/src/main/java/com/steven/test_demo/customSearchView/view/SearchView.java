package com.steven.test_demo.customSearchView.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.steven.test_demo.R;
import com.steven.test_demo.customSearchView.callback.IReturnCallback;
import com.steven.test_demo.customSearchView.callback.ISearchCallback;
import com.steven.test_demo.customSearchView.utils.RecordSQLiteHelper;

public class SearchView extends LinearLayout {
    private Context context;

    // 数据库
    private RecordSQLiteHelper helper;
    private SQLiteDatabase db;

    // listview+适配器
    private SearchListView listView;
    private BaseAdapter adapter;

    // 回调接口
    private ISearchCallback searchCallback;
    private IReturnCallback returnCallback;

    // 自定义属性
    private Float textSizeSearch;
    private int textColorSearch;
    private String textHintSearch;

    // 搜索框设置
    private int searchBlockHeight;
    private int searchBlockColor;

    // 搜索框组件
    private EditText etSearch; // 搜索按键
    private TextView tvClear;  // 删除搜索记录按键
    private LinearLayout searchBlock; // 搜索框布局
    private ImageView searchBack; // 返回按键

    public SearchView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public SearchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initAttrs(context,attrs);
        init();
    }

    public SearchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initAttrs(context, attrs);
        init();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        // 控件资源名称
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Search_View);

        // 搜索框字体大小（dp）
        textSizeSearch = typedArray.getDimension(R.styleable.Search_View_textSizeSearch, 20);

        // 搜索框字体颜色（使用十六进制代码，如#333、#8e8e8e）
        int defaultColor = context.getResources().getColor(R.color.black); // 默认颜色 = 灰色
        textColorSearch = typedArray.getColor(R.styleable.Search_View_textColorSearch, defaultColor);

        // 搜索框提示内容（String）
        textHintSearch = typedArray.getString(R.styleable.Search_View_textHintSearch);

        // 搜索框高度
        searchBlockHeight = typedArray.getInteger(R.styleable.Search_View_searchBlockHeight, 150);

        // 搜索框颜色
        int defaultColor2 = context.getResources().getColor(R.color.white); // 默认颜色 = 白色
        searchBlockColor = typedArray.getColor(R.styleable.Search_View_searchBlockColor, defaultColor2);

        // 释放资源
        typedArray.recycle();
    }

    private void init() {
        initView();
        helper = new RecordSQLiteHelper(context);
        // queryData(); // 首次进入查询所有历史记录
        tvClear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                deleteData();
//                queryData();
            }
        });
        // 监听输入键盘的按键事件
        // 点击enter键时触发回调
        etSearch.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // 点击键盘回车键 && 手指触发键盘点击事件
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    String inputMsg = etSearch.getText().toString();
                    if (searchCallback != null) {
                        searchCallback.onSearch(inputMsg); //搜索事件回调
                    }
                    toast(context, "搜索：" + inputMsg);
                    //boolean exists =  checkData(etSearch.getText().toString().trim());
//                    if (!exists) {
//                        insertData(inputMsg);
//                        queryData("");
//                    }
                }
                return false;
            }
        });
        // 监听文本实时变化
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // 输入文本后回调此方法
                String msg = etSearch.getText().toString();
//                query(msg);
            }
        });
        // 搜索记录列表
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 获取用户点击列表里的文字,并自动填充到搜索框内
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                String name = textView.getText().toString();
                if (!TextUtils.isEmpty(name)) {
                    etSearch.setText(name);
                    toast(context, name);
                }
            }
        });
        searchBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (returnCallback != null) {
                    returnCallback.onReturn();
                }
                // 自定义实现
            }
        });
    }

    private void initView() {
        // 初始化布局
        LayoutInflater.from(context).inflate(R.layout.search_layout, this);
        // 初始化搜索框
        etSearch = (EditText) findViewById(R.id.et_search);
        etSearch.setTextSize(textSizeSearch);
        etSearch.setTextColor(textColorSearch);
        etSearch.setHint(textHintSearch);
        // 设置参数
        searchBlock = (LinearLayout) findViewById(R.id.search_block);
        ViewGroup.LayoutParams layoutParams = searchBlock.getLayoutParams();
        layoutParams.height = searchBlockHeight;
        searchBlock.setBackgroundColor(searchBlockColor);
        searchBlock.setLayoutParams(layoutParams);
        // 历史搜索记录的Listview
        listView = (SearchListView) findViewById(R.id.listView);
        // 删除历史记录按钮
        tvClear = (TextView) findViewById(R.id.tv_clear);
        tvClear.setVisibility(INVISIBLE);
        // 返回按钮
        searchBack = (ImageView) findViewById(R.id.search_back);
    }

    private void toast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
}
