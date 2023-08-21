package com.myapplication

import android.app.Application
import di.androidDiModule
import di.initKoin

class BLEApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin(this)
    }
}