// IOnBookGetListener.aidl
package com.steven.aidl_demo;

// 导入使用的AIDL的全类名
import com.steven.aidl_demo.Book;

interface IOnBookGetListener {
    void onNewBookArrived(in Book book);
}