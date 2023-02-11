package com.steven.jetpack_demo.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * 基于ViewModelProvider.Factory实现想View Model实例传递初始化参数
 */
class ViewModelFactory(private val countReserved: Int): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(countReserved) as T
    }
}