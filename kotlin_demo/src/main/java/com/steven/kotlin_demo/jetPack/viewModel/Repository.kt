package com.steven.kotlin_demo.jetPack.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object Repository {
    fun getUser(userId: String): LiveData<User>{
        val liveData = MutableLiveData<User>()
        liveData.value = User(userId, userId, 0)
        return liveData
    }

    fun getUserName(userName: String): LiveData<User>{
        val liveData = MutableLiveData<User>()
        liveData.value = User(userName, userName, 0)
        return liveData
    }
}