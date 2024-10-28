package com.fintacharts

import android.app.Application
import com.fintacharts.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class ThisApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ThisApplication)
            androidLogger()

            modules(appModule)
        }

    }
}