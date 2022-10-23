package com.steven.content_provider_demo.custom_contentprovider;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.steven.content_provider_demo.R;

import java.util.Random;

public class CustomCPActivity extends AppCompatActivity {
    private String newId;
    private static final String path = "content://com.steven.content_provider_demo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_cpactivity);

        Button add_data = findViewById(R.id.add_data);
        add_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(path + "/book");
                ContentValues values = new ContentValues();
                Random random = new Random();
                int random_num = random.nextInt(1000);
                values.put("name","Steven" + String.valueOf(random_num));
                values.put("author","no body" + String.valueOf(random_num));
                values.put("pages",1000);
                values.put("price",22.9);
                Uri newUri = getContentResolver().insert(uri, values);
                newId = newUri.getPathSegments().get(1);
                Toast.makeText(CustomCPActivity.this, "insert new data id = "+newId, Toast.LENGTH_SHORT).show();
            }
        });

        Button query_data = findViewById(R.id.query_data);
        query_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(path + "/book");
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                if (cursor!=null){
                    while (cursor.moveToNext()){
                        String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                        String author = cursor.getString(cursor.getColumnIndexOrThrow("author"));
                        int pages = cursor.getInt(cursor.getColumnIndexOrThrow("pages"));
                        double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
                        Log.d("CustomCP#", "book name is " + name);
                        Log.d("CustomCP#", "book author is " + author);
                        Log.d("CustomCP#", "book pages is " + pages);
                        Log.d("CustomCP#", "book price is " + price);
                    }
                    cursor.close();
                }
            }
        });

        Button update_data = findViewById(R.id.update_data);
        update_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(path+"/book"+newId);
                ContentValues values = new ContentValues();
                values.put("name", "steven ssss");
                getContentResolver().update(uri,values,null,null);
                Log.d("CustomCP#","update row id = "+newId);
            }
        });

        Button delete_data = findViewById(R.id.delete_data);
        delete_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(path + "/book" + newId);
                getContentResolver().delete(uri,null,null);
                Log.d("CustomCP#","delete row id = "+newId);
            }
        });
    }
}