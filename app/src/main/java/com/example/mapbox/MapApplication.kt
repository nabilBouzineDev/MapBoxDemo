package com.example.mapbox

import android.app.Application
import com.example.mapbox.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MapApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MapApplication)
            modules(listOf(appModule))
        }
    }
}