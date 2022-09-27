package com.dodi.myservice

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import com.dodi.myservice.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private var boundStatus = false
    private lateinit var boundService: MyBoundService

    private val connection = object : ServiceConnection{
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            val myBinder = p1 as MyBoundService.MyBinder
            boundService = myBinder.getService
            boundStatus = true
            getNumberFromService()
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            boundStatus = false
        }

    }

    private fun getNumberFromService(){
        boundService.numberLiveData.observe(this){
            binding.tvBoundServiceNumber.text = it.toString()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val serviceIntent = Intent(this,MyBackgroundService::class.java)
        val foregroundService = Intent(this, MyForegroundService::class.java)
        val boundServiceIntent = Intent(this, MyBoundService::class.java)

        binding.btnStartBoundService.setOnClickListener {
            bindService(boundServiceIntent, connection, BIND_AUTO_CREATE)
        }

        binding.btnStopBoundService.setOnClickListener {
            unbindService(connection)
        }

        binding.btnStartBackgroundService.setOnClickListener {
            startService(serviceIntent)
        }

        binding.btnStopBackgroundService.setOnClickListener {
            stopService(serviceIntent)
        }

        binding.btnStartForegroundService.setOnClickListener {
            if (Build.VERSION.SDK_INT >=26){
                startForegroundService(foregroundService)
            }else{
                startService(foregroundService)
            }
        }

        binding.btnStopForegroundService.setOnClickListener {
            stopService(foregroundService)
        }
    }

    override fun onStop() {
        super.onStop()
        if (boundStatus){
            unbindService(connection)
            boundStatus = false
        }
    }
}