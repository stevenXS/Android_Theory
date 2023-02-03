package com.steven.android_theory.MVP;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.steven.android_theory.R;

interface IFanyiView{
    void init();
    void setInfo(String input);
    void setError();
}

public class MainActivityView extends AppCompatActivity implements IFanyiView{
    private EditText input;
    private TextView show;
    private ICidianPresenter cidianPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cidianPresenter.inputToModel(input.getText().toString(), MainActivityView.this);
            }
        });
    }

    @Override
    public void init() {
        input = (EditText) findViewById(R.id.input_english);
        show = (TextView) findViewById(R.id.show_chinese);
        // 将View层实现传递给Presenter层，由它来控制View&Model的交互
        cidianPresenter = new CidianPresenter(this);
    }

    @Override
    public void setInfo(String input) {
        show.setText("结果：" + input);
    }

    @Override
    public void setError() {
        show.setText("Error");
    }
}