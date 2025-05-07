package com.example.factsnumberstask.main

import android.app.Application
import com.example.factsnumberstask.BuildConfig
import com.example.factsnumberstask.numbers.data.CloudModule

class NumbersApp : Application() {

    override fun onCreate() {
        super.onCreate()

        //todo move out of here

        val cloudModule = if (BuildConfig.DEBUG)
            CloudModule.Debug()
        else
            CloudModule.Release()
    }

}