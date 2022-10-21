package com.steven.content_provider_demo.phone_directory;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.steven.content_provider_demo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 通过contentProvider读取电话簿的联系人
 */
public class ReadPhoneBookActivity extends AppCompatActivity {
    private List<String> contacts = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 简易版的RecycleView（ListVIew）: 1.声明listView视图；2.给视图添加子项item视图；3.设置交互的ArrayList Adapter。
        ListView listView = findViewById(R.id.listview_show_contacts);
        adapter = new ArrayAdapter<>(ReadPhoneBookActivity.this, android.R.layout.simple_list_item_1, contacts);
        listView.setAdapter(adapter);
        // 判断READ_CONTACTS权限
        if (ContextCompat.checkSelfPermission(ReadPhoneBookActivity.this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED){
            // 隐私弹窗是异步处理（开启一个子线程，然后回调处理结果）
            ActivityCompat.requestPermissions(ReadPhoneBookActivity.this,
                    new String[]{Manifest.permission.READ_CONTACTS}, 10);
        }else {
            readContacts();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 10:
                if (permissions.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    readContacts();
                }else {
                    Toast.makeText(ReadPhoneBookActivity.this, "permission denied.", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    public void readContacts(){
        Cursor cursor = null;
        try {
            ContentResolver contentResolver = getContentResolver();
            cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            if (cursor != null ){
                while (cursor.moveToNext()){
                    // 联系人姓名
                    String display_name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    // 联系人手机号
                    String phone_number = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    contacts.add(display_name + "\n" + phone_number);
                }
                adapter.notifyDataSetChanged(); // 回调
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(cursor != null)
                cursor.close();
        }

    }
}
