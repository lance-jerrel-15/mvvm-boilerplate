package com.mvvm.boilerplate

import android.app.Application
import androidx.paging.ExperimentalPagingApi
import com.mvvm.boilerplate.di.appModules
import com.mvvm.boilerplate.di.dataSourceModules
import com.mvvm.boilerplate.di.networkModules
import com.mvvm.boilerplate.di.viewModelModules
import org.koin.android.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {

    @ExperimentalPagingApi
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            if (BuildConfig.DEBUG) {
                androidLogger()
            }
            modules(listOf(appModules, networkModules, viewModelModules, dataSourceModules))
        }
    }
}