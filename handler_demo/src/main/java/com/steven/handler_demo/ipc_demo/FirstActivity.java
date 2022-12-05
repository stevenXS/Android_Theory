package com.steven.handler_demo.ipc_demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.Serializable;

/**
 * 基于序列化在Intent中传输对象
 */
public class FirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Person person = new Person();
        Student student = new Student();
        person.setAge(10);
        student.setName("steven");
        student.setAge(24);

        Intent intent = new Intent(FirstActivity.this, SecondActivity.class);
        intent.putExtra("person", person);
        intent.putExtra("student", student);
        startActivity(intent);
    }
}