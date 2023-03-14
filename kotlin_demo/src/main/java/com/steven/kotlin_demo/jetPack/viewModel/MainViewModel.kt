package com.steven.kotlin_demo.jetPack.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    var counter = 0
    val counters: LiveData<Int> get() = _counter
    private val _counter = MutableLiveData<Int>()

    // <editor-folder desc="map">
    private val userId = MutableLiveData<User>()
    val userName: LiveData<String> = Transformations.map(userId){user ->
        "${user.firstName}+${user.lastName}" // 只显示User类的某个属性
    }
    fun getUserName(name:String){
        userId.value = User(name, name, 0)
    }
    // </editor-folder>

    // <editor-folder desc="SwitchMap">
    private val userIdLiveData = MutableLiveData<String>()
    val user: LiveData<User> = Transformations.switchMap(userIdLiveData) { userId ->
        Repository.getUser(userId) //将liveData对象转化为一个可观察的liveData对象
    }
    fun getUser(userId: String){
        userIdLiveData.value = userId
    }
    // </editor-folder>

}