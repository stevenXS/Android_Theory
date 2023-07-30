package com.steven.test_demo.customSearchView.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class RecordSQLiteHelper extends SQLiteOpenHelper {
    private static final String name = "search.db";
    private static final int version = 2;

    private static final String createTable = "create table search(id integer primary key autoincrement,name varchar(200))";

    public RecordSQLiteHelper(@Nullable Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
