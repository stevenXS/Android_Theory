package com.steven.kotlin_demo.content_provider

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.contentValuesOf
import com.steven.kotlin_demo.CommonActivity
import com.steven.kotlin_demo.R
import kotlinx.android.synthetic.main.activity_my_provider.*

class MyContentProviderActivity: CommonActivity() {
    private val contactsList = ArrayList<String>()
    private lateinit var adapter: ArrayAdapter<String>
    private var bookId: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_my_provider)
//        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, contactsList)
//        contactsView.adapter = adapter
//        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS)
//                != PackageManager.PERMISSION_GRANTED
//        ){
//            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_CONTACTS, android.Manifest.permission.WRITE_CONTACTS),123)
//        }else{
//            readContacts()
//        }
        addView("addData", "", Button(this)){
            val uri = Uri.parse("content://${MyProvider.authority}/book")
            val values = contentValuesOf(
                "name" to "A clash of kings",
                "author" to "George Martin",
                "pages" to 5555,
                "price" to 22.85
            )
            val newUri = contentResolver.insert(uri, values)
            bookId = newUri?.pathSegments?.get(1)
            toast("insert# get new book id is $bookId")
        }
        addView("queryData", "", Button(this)){
            val uri = Uri.parse("content://${MyProvider.authority}/book")
            contentResolver.query(uri, null, null, null, null)?.apply {
                while (moveToNext()){
                    val name = getString(getColumnIndexOrThrow("name"))
                    val author = getString(getColumnIndexOrThrow("author"))
                    val pages = getString(getColumnIndexOrThrow("pages"))
                    val price = getString(getColumnIndexOrThrow("price"))
                    toast("name ${getColumnIndexOrThrow("name")}")
                    toast("author ${getColumnIndexOrThrow("author")}")
                    toast("pages ${getColumnIndexOrThrow("pages")}")
                    toast("price ${getColumnIndexOrThrow("price")}")
                    toast("query book name is $name")
                    toast("query book author is $author")
                    toast("query book pages is $pages")
                    toast("query book price is $price")
                }
                close()
            }
        }
        addView("updateData", "", Button(this)){
            // 更新数据
            bookId?.let {
                val uri = Uri.parse("content://${MyProvider.authority}/book/$it")
                val values = contentValuesOf("name" to "A Storm of Swords",
                        "pages" to 1216, "price" to 24.05)
                contentResolver.update(uri, values, null, null)
            }
        }
        addView("deleteData", "", Button(this)){
            // 删除数据
            bookId?.let {
                val uri = Uri.parse("content://${MyProvider.authority}/book/$it")
                contentResolver.delete(uri, null, null)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            123 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    readContacts()
                }else{
                    toast("need permission.")
                }
            }
        }
    }

    private fun readContacts() {
        // 查询联系人数据
        contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                null, null, null)?.apply {
            while (moveToNext()){
                // 获取联系人姓名
                val displayName = getString(getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val number = getString(getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
                contactsList.add("${displayName}\n${number}")
            }
            // 刷新数据
            adapter.notifyDataSetChanged()
            close()
        }
    }
}