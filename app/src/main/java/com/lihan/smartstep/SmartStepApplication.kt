package com.lihan.smartstep

import android.app.Application
import com.lihan.smartstep.core.di.coreModule
import com.lihan.smartstep.profile_setup.di.profileSetupModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class SmartStepApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@SmartStepApplication)
            androidLogger(Level.DEBUG)
            modules(
                listOf(
                    coreModule,
                    profileSetupModule
                )
            )
        }
    }
}