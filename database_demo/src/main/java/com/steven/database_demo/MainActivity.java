package com.steven.database_demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.ContentObservable;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private MyDBHelper myDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button add = findViewById(R.id.add_data);
        myDBHelper = new MyDBHelper(this, "BookStore.db", null, 2);
        SQLiteDatabase wd = myDBHelper.getWritableDatabase();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put("name","哈利波特");
                values.put("author","steven");
                values.put("price",30.0);
                wd.insert("Book",null, values);
                values.clear();

                values.put("name","小王子");
                values.put("author","steven");
                values.put("price",90.0);
                wd.insert("Book",null, values);
                Toast.makeText(MainActivity.this, "add done!", Toast.LENGTH_SHORT).show();
            }
        });

        Button update = findViewById(R.id.update_data);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put("name","ricky");
                wd.update("Book",values, "name=?",
                        new String[]{"steven"});
                Toast.makeText(MainActivity.this, "update done!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}