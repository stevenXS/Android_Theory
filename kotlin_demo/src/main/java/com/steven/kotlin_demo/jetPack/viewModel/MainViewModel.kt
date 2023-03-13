package com.steven.kotlin_demo.jetPack.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    var counter = 0
    val counters: LiveData<Int> get() = _counter
    private val _counter = MutableLiveData<Int>()
}