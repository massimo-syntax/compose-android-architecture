package com.example.fruits_room_hilt.di

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// we should specify this class in our manifest file under application.
// android:name=".FruitApplication"
@HiltAndroidApp
class FruitApplication: Application() {
}