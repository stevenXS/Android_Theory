package com.steven.handler_demo.ipc_demo;

import android.os.Parcel;
import android.os.Parcelable;

public class Student implements Parcelable {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    private String name;
    private int age;


    public static final Creator<Student> CREATOR = new Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel in) {
            Student student = new Student();
            student.name = in.readString(); // 读
            student.age = in.readInt(); // 读
            return student;
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // 写值
        dest.writeString(name);
        dest.writeInt(age);
    }
}
