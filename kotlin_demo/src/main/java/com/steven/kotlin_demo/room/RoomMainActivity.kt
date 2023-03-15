package com.steven.kotlin_demo.room

import android.os.Bundle
import android.util.Log
import com.steven.kotlin_demo.CommonActivity
import com.steven.kotlin_demo.R
import kotlinx.android.synthetic.main.activity_room_main.*
import kotlin.concurrent.thread

class RoomMainActivity : CommonActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_main)
        val userDao = AppDataBase.getDatabase(this).userDao()
        val user1 = User("Tom", "Bray", 40)
        val user2 = User("Tom", "Hanks", 63)
        addDataBtn.setOnClickListener {
            thread {
                user1.id = userDao.insertUser(user1)
                user2.id = userDao.insertUser(user2)
            }
        }
        updateDataBtn.setOnClickListener {
            thread {
                user1.age = 42
                userDao.updateUser(user1)
            }
        }
        deleteDataBtn.setOnClickListener {
            thread {
                userDao.deleteUserByLastName("Hanks")
            }
        }
        queryDataBtn.setOnClickListener {
            thread {
                for (user in userDao.loadAllUsers()) {
                    Log.d("MainActivity", user.toString())
                }
            }
        }
    }
}