package com.steven.content_provider_demo.database_helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "MyDatabaseHelper#";

    private static final String CREATE_BOOK = "create table Book(" +
            "id integer primary key autoincrement, " +
            "author text, " +
            "name text, " +
            "pages integer, " +
            "price real)";
    private static final String CREATE_CATEGORY = "create table Category(" +
            "id integer primary key autoincrement, " +
            "author text, " +
            "name text, " +
            "price real)";

    private Context mContext;

    public MyDatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BOOK);
//        db.execSQL(CREATE_CATEGORY);
        Log.d(TAG,"database created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists Book");
//        db.execSQL("drop table if exists Category");
        onCreate(db);
        Log.d(TAG,"upgrade database");
    }

}
