package com.lihan.smartstep.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.work.Configuration
import com.lihan.smartstep.core.di.coreModule
import com.lihan.smartstep.core.service.SmartStepForegroundService.Companion.CHANNEL_ID
import com.lihan.smartstep.core.worker.SaveDailyStepsScheduler
import com.lihan.smartstep.dashboard.di.dashboardModule
import com.lihan.smartstep.profile_setup.di.profileSetupModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.factory.KoinWorkerFactory
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class SmartStepApplication: Application(), Configuration.Provider, KoinComponent {
    override fun onCreate() {
        super.onCreate()

        createNotificationChannel()

        startKoin {
            androidContext(this@SmartStepApplication)
            androidLogger(Level.DEBUG)
            workManagerFactory()
            modules(
                listOf(
                    coreModule,
                    profileSetupModule,
                    dashboardModule
                )
            )
        }

        SaveDailyStepsScheduler.scheduleWork(this)
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(get<KoinWorkerFactory>())
            .setMinimumLoggingLevel(android.util.Log.DEBUG)
            .build()

    private fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channelId = CHANNEL_ID
            val name = "SmartStep's Notifications"
            val descriptionText = "Notifications for SmartStep App"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}