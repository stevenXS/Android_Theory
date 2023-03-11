package com.steven.kotlin_demo.service

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Binder
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import com.steven.kotlin_demo.CommonActivity
import com.steven.kotlin_demo.R
import kotlinx.android.synthetic.main.activity_service_main.*

class ServiceMainActivity : CommonActivity() {
    lateinit var download: MyService.DownloadBinder
    private val connect = object: ServiceConnection{
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.d("MyService", "onServiceConnected")
            download = service as MyService.DownloadBinder
            download.startDownload()
            download.getProgress()
        }
        // service进程崩溃或者被杀掉时调用
        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d("MyService", "onServiceConnected")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_main)
        // 如果同时调用startService&bindService；
        // 此时需要同时调用stopService和unBindService
        startService.setOnClickListener {
            val intent = Intent(this, MyService::class.java)
            startService(intent)
        }
        stopService.setOnClickListener {
            val intent = Intent(this, MyService::class.java)
            stopService(intent)
        }
        bindService.setOnClickListener {
            val intent = Intent(this, MyService::class.java)
            bindService(intent, connect, Context.BIND_AUTO_CREATE)
        }
        unBindService.setOnClickListener {
            unbindService(connect)
        }
    }

    class MyService: Service(){
        private val mBinder = DownloadBinder()

        class DownloadBinder:Binder() {
            fun startDownload(){
                Log.d("MyService", "start down load")
            }

            fun getProgress(){
                Log.d("MyService", "get Progress")
            }
        }

        // startService时执行一次
        override fun onCreate() {
            super.onCreate()
            Log.d("MyService", "onCreate executed")
        }

        // 每次调用startService都会执行
        override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
            Log.d("MyService", "onStartCommand executed")
            return super.onStartCommand(intent, flags, startId)
        }

        // stopService时执行一次
        override fun onDestroy() {
            super.onDestroy()
            Log.d("MyService", "onDestroy executed")
        }

        // 绑定binder时执行
        override fun onBind(intent: Intent?): IBinder? {
            Log.d("MyService", "onBind")
            return mBinder
        }
    }
}