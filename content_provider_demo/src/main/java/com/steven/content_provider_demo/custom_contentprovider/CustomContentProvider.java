package com.steven.content_provider_demo.custom_contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.steven.content_provider_demo.database_helper.MyDatabaseHelper;

public class CustomContentProvider extends ContentProvider {
    private static final int BOOK_DIR = 0;
    private static final int BOOK_ITEM = 1;
    private static final int CATEGORY_DIR = 2;
    private static final int CATEGORY_ITEM = 3;

    private static UriMatcher uriMatcher;
    private static final String AUTHORITY = "com.steven.content_provider_demo";
    private MyDatabaseHelper databaseHelper;

    // 初始化资源拦截器
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY,"book", BOOK_DIR);
        uriMatcher.addURI(AUTHORITY,"book/#", BOOK_ITEM);
        uriMatcher.addURI(AUTHORITY,"category", CATEGORY_DIR);
        uriMatcher.addURI(AUTHORITY,"category/#", CATEGORY_ITEM);
    }

    @Override
    public boolean onCreate() {
        // 创建contentprovider时创建数据库对象
        databaseHelper = new MyDatabaseHelper(getContext(), "BookStore.db", null, 2);
        Log.d("CustomContentProvider#","onCreate");
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase readableDatabase = databaseHelper.getReadableDatabase();
        Cursor cursor = null;
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
                cursor = readableDatabase.query("Book", projection, selection, selectionArgs,null,null,sortOrder);
                break;
            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                readableDatabase.query("Book", projection, "id=?", new String[]{bookId},null,null,sortOrder);
                break;
            case CATEGORY_DIR:
                cursor = readableDatabase.query("Category", projection, selection, selectionArgs,null,null,sortOrder);
                break;
            case CATEGORY_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                readableDatabase.query("Category", projection, "id=?", new String[]{categoryId},null,null,sortOrder);
                break;
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
                // 查询的Uri以路径结尾时返回的MIME类型
                return "vnd.android.cursor.dir/vnd.com.steven.content_provider_demo.book";
            case BOOK_ITEM:
                // 查询Uri以ID结尾时返回的MIME类型
                return "vnd.android.cursor.item/vnd.com.steven.content_provider_demo.book";
            case CATEGORY_DIR:
                return "vnd.android.cursor.dir/vnd.com.steven.content_provider_demo.category";
            case CATEGORY_ITEM:
                return "vnd.android.cursor.item/vnd.com.steven.content_provider_demo.category";
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase writableDatabase = databaseHelper.getWritableDatabase();
        Uri res = null;
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
            case BOOK_ITEM:
                long bookId = writableDatabase.insert("Book", null, values);
                res = Uri.parse("content://"+AUTHORITY+"/book/"+bookId);
                break;
            case CATEGORY_DIR:
            case CATEGORY_ITEM:
                long categoryId = writableDatabase.insert("Category", null, values);
                res = Uri.parse("content://"+AUTHORITY+"/category/"+categoryId);
                break;
        }
        return res;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase writableDatabase = databaseHelper.getWritableDatabase();
        int updatedRows = 0;
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
                updatedRows = writableDatabase.update("Book", values, selection, selectionArgs );
                break;
            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                updatedRows = writableDatabase.update("Book", values,"id=?", new String[]{bookId});
                break;
            case CATEGORY_DIR:
                updatedRows = writableDatabase.update("Category", values, selection, selectionArgs );
                break;
            case CATEGORY_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                updatedRows = writableDatabase.update("Category", values,"id=?", new String[]{categoryId});
                break;
        }
        return updatedRows;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase writableDatabase = databaseHelper.getWritableDatabase();
        int deletedRows = 0;
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
                deletedRows = writableDatabase.delete("Book", selection, selectionArgs);
                break;
            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                deletedRows = writableDatabase.delete("Book", "id=?", new String[]{bookId});
                break;
            case CATEGORY_DIR:
                deletedRows = writableDatabase.delete("Category", selection, selectionArgs);
                break;
            case CATEGORY_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                deletedRows = writableDatabase.delete("Category", "id=?", new String[]{categoryId});
                break;
        }
        return deletedRows;
    }

}
