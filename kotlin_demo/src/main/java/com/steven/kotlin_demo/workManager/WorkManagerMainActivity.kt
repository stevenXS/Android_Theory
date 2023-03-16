package com.steven.kotlin_demo.workManager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.steven.kotlin_demo.R
import kotlinx.android.synthetic.main.activity_work_manager_main.*

class WorkManagerMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work_manager_main)
        val request = OneTimeWorkRequest.Builder(SimpleWorker::class.java).build()

        doWorkBtn.setOnClickListener {
            WorkManager.getInstance(this).enqueue(request)
        }

        WorkManager.getInstance(this)
                .getWorkInfoByIdLiveData(request.id)
                .observe(this){
                    if (it.state == WorkInfo.State.SUCCEEDED){
                        show_res.text = "do work succeeded"
                        Log.d("MainActivity", "do work succeeded")
                    }
                }
    }
}