package com.lihan.smartstep

import android.app.Application
import com.lihan.smartstep.core.di.coreModule
import com.lihan.smartstep.stepcount.di.stepCountModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.android.logger.AndroidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.logger.Logger

class SmartStepApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@SmartStepApplication)
            modules(
                listOf(
                    coreModule,
                    stepCountModule
                )
            )
        }
    }
}