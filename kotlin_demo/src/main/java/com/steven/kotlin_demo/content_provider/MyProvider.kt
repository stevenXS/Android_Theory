package com.steven.kotlin_demo.content_provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri

class MyProvider: ContentProvider() {
    companion object{
        private val bookDir = 0
        private val bookItem = 1
        private val categoryDir = 2
        private val categoryItem = 3
        const val authority = "com.steven.kotlin_demo"
        private var dbHelper: MyDatabaseHelper? = null
    }

    /**
        by lazy代码块里对UriMatcher进行了初始化操作，将期望匹配的几种
        URI格式添加了进去。by lazy代码块是Kotlin提供的一种懒加载技术，代码块中的代码一开始
        并不会执行，只有当uriMatcher变量首次被调用的时候才会执行，并且会将代码块中最后一
        行代码的返回值赋给uriMatcher。
     */
    private val uriMatcher by lazy{
        // 初始化待匹配的路劲
        val matcher = UriMatcher(UriMatcher.NO_MATCH)
        matcher.addURI(authority, "book", bookDir)
        matcher.addURI(authority, "book/#", bookItem)
        matcher.addURI(authority, "category", categoryDir)
        matcher.addURI(authority, "category/#", categoryItem)
        matcher
    }

    override fun onCreate()=context?.let {
        dbHelper = MyDatabaseHelper(it, "BookStore.db", 2)
        true
    }?: false


    override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor? {
        var cursor: Cursor? = null
        dbHelper?.let {
            val db = it.readableDatabase
            cursor = when(uriMatcher.match(uri)){
                bookDir -> return db.query("Book", projection, selection, selectionArgs,
                        null, null, sortOrder)
                bookItem -> {
                    val bookId = uri.pathSegments[1]
                    return db.query("Book", projection, "id = ?", arrayOf(bookId), null, null,
                            sortOrder)
                }
                categoryDir -> return db.query("Category", projection, selection, selectionArgs,
                        null, null, sortOrder)
                categoryItem ->{
                    val categoryId = uri.pathSegments[1]
                    return db.query("Category", projection, "id = ?", arrayOf(categoryId),
                            null, null, sortOrder)
                }
                else -> return null
            }
        }
        return cursor
    }

    /**
     * URI所对应的MIME字符串主要由3部分组成，Android对这3个部分做了如下格式规定。必须以vnd开头:
        如果内容URI以路径结尾，则后接android.cursor.dir/；
        如果内容URI以id结尾，则后接android.cursor.item/。
        最后接上vnd.<authority>.<path>。
     */
    override fun getType(p0: Uri): String? {
        when (uriMatcher.match(p0)) {
            bookDir -> return "vnd.android.cursor.dir/vnd.$authority.book"
            bookItem ->return "vnd.android.cursor.item/vnd.$authority.book"
            categoryDir -> return "vnd.android.cursor.dir/vnd.$authority.category"
            categoryItem -> return "vnd.android.cursor.item/vnd.$authority.category"
            else -> return null
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        var res: Uri? = null
        dbHelper?.let {
            val db = it.writableDatabase
            res = when(uriMatcher.match(uri)){
                bookDir, bookItem -> {
                    val newBookId = db.insert("Book", null, values) // 返回插入新数据的rowId
                    return Uri.parse("content://$authority/book/$newBookId")
                }
                categoryDir,categoryItem -> {
                    val newCategoryId = db.insert("Category", null, values)
                    return Uri.parse("content://$authority/category/$newCategoryId")
                }
                else -> return null
            }
        }
        return res
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?) = dbHelper?.let {
        val db = it.writableDatabase
        val deletedRows = when (uriMatcher.match(uri)) {
            bookDir -> db.delete("Book", selection, selectionArgs)
            bookItem -> {
                val bookId = uri.pathSegments[1]
                db.delete("Book", "id = ?", arrayOf(bookId))
            }
            categoryDir -> db.delete("Category", selection, selectionArgs)
            categoryItem -> {
                val categoryId = uri.pathSegments[1]
                db.delete("Category", "id = ?", arrayOf(categoryId))
            }
            else -> 0
        }
        deletedRows
    }?: 0

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?)= dbHelper?.let {
        val db = it.writableDatabase
        // 传入的uri与初始化的uriMatcher列表进行匹配
        val updateRows = when(uriMatcher.match(uri)){
            bookDir -> db.update("Book", values, selection, selectionArgs)
            bookItem -> {
                val categoryId = uri.pathSegments[1] // 基于“/”分割字符串
                db.update("Book", values, "id = ?", arrayOf(categoryId))
            }
            categoryDir -> db.update("Category", values, selection, selectionArgs)
            categoryItem -> {
                val categoryId = uri.pathSegments[1]
                db.update("Category", values, "id = ?", arrayOf(categoryId))
            }
            else -> 0
        }
        updateRows
    }?: 0 //不为空返回updateRows；反之返回0

}