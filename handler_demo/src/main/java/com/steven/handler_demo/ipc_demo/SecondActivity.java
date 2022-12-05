package com.steven.handler_demo.ipc_demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.io.Serializable;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Person person = (Person)getIntent().getSerializableExtra("person");
        Student student = (Student) getIntent().getParcelableExtra("student");
        Log.d("Second", student.getName()+" "+student.getAge());
        Log.d("Second", person.getAge()+"");
    }
}