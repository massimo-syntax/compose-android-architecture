package com.example.mock_interceptor_retrofit

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// This annotation triggers Hilt code generation
// It creates a base class that your Application extends at compile time
@HiltAndroidApp
class MyApplication : Application()