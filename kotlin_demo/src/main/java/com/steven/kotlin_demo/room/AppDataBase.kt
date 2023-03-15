package com.steven.kotlin_demo.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(version = 1, entities = [User::class])
abstract class AppDataBase: RoomDatabase() {
    // room 底层自动完成
    abstract fun userDao(): UserDao

    companion object{
        private var instance: AppDataBase? = null

        @Synchronized
        fun getDatabase(context: Context): AppDataBase{
            instance?.let {
                return it
            }
            return Room.databaseBuilder(context.applicationContext,
            AppDataBase::class.java, "app_database").build().apply {
                instance = this
            }
        }
    }
}