package com.steven.content_provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "steven_demo.db";
    private static final int DATABASE_VERSION = 1;
    private static final String CREATE_TABLE = "create table if not exists " +
            Constant.TABLE_NAME + "(" +
            Constant.COLUMN_ID + " integer primary key autoincrement, " +
            Constant.COLUMN_NAME + " varchar not null);";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + Constant.TABLE_NAME + ";");
        onCreate(db);
    }
}
