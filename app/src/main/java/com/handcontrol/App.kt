package com.handcontrol

import android.app.Application
import com.handcontrol.api.Api

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Api.init(applicationContext)
    }
}