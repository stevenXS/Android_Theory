// IBookManager.aidl
package com.steven.aidl_demo;

// 导入使用的AIDL的全类名
import com.steven.aidl_demo.Book;
import com.steven.aidl_demo.IOnBookGetListener;

interface IBookManager {
    // 定义一些接口方法
    List<Book> getBookList();
    void addBook(in Book book);
    void registerListener(IOnBookGetListener listener);
    void unRegisterListener(IOnBookGetListener listener);
}