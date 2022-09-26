package com.dodi.myservice

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dodi.myservice.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val serviceIntent = Intent(this,MyBackgroundService::class.java)
        val foregroundService = Intent(this, MyForegroundService::class.java)

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
}