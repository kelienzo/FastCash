package com.kelly.fastcash

import android.app.Application
import com.kelly.fastcash.di.initKoin
import com.kelly.fastcash.di.uiModule
import org.koin.android.ext.koin.androidContext

class FastCashApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@FastCashApp)
            modules(uiModule)
        }
    }
}
