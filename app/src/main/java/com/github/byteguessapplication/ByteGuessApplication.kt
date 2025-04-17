package com.github.byteguessapplication

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ByteGuessApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}