package com.example.uberv.githubbrowsersamplekt

import android.app.Application
import com.example.uberv.githubbrowsersamplekt.utils.DevelopmentTree
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(DevelopmentTree())
        }
    }
}