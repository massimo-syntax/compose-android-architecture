package com.example.multimodularmodernarchitecturefullapp

import android.app.Application
import android.util.Log
import com.example.utils.logging.AppLogger
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        AppLogger.d(message = "Application launched, App -> onCreate() invoked")
    }

    companion object{
        val TAG = "App"
    }
}