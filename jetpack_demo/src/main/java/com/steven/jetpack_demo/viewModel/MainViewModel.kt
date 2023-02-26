package com.steven.jetpack_demo.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel(countReserved: Int): ViewModel() {
    // 基于LiveData类可以在数据发生变化时主动通知宿主
    var counter = MutableLiveData<Int>()

    init {
        counter.value = countReserved
    }

    fun plusOne(){
        val count = counter.value?: 0
        counter.value = count + 1
    }

    fun clear(){
        counter.value = 0
    }
}