package com.example.myapplication

import android.app.Application
import com.example.myapplication.core.di.databaseModule
import com.example.myapplication.core.di.networkModule
import com.example.myapplication.core.di.repositoryModule
import com.example.myapplication.di.useCaseModule
import com.example.myapplication.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.loadKoinModules
import org.koin.core.logger.Level

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@MyApplication)
            modules(
                listOf(
                    databaseModule,
                    networkModule,
                    repositoryModule,
                    useCaseModule,
                    viewModelModule,

                )
            )
        }

    }

}