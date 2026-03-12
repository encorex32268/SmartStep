package com.lihan.smartstep

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.lihan.smartstep.core.di.coreModule
import com.lihan.smartstep.stepcount.di.stepCountModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class SmartStepApplication: Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    companion object{
        const val CHANNEL_ID = "smart_step"
    }
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
        if (BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }

        createNotificationChannel()
    }

    private fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = this.getString(R.string.app_name)
            val channel = NotificationChannel(CHANNEL_ID,name, NotificationManager.IMPORTANCE_DEFAULT)
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}