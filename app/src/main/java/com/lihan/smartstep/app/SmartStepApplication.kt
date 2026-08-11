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

import com.lihan.smartstep.core.database.DailyStepEntity
import com.lihan.smartstep.core.database.DailyStepsDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import kotlin.math.round
import kotlin.random.Random

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

        seedMockDailyStepsData()

        SaveDailyStepsScheduler.scheduleWork(this)
    }

    private fun seedMockDailyStepsData() {
        val dailyStepsDao: DailyStepsDao = get()
        val scope: CoroutineScope = get()
        scope.launch(Dispatchers.IO) {
            val currentYear = LocalDate.now().year
            val zoneId = ZoneId.systemDefault()
            val monthsToSeed = listOf(7, 8 ,9)

            monthsToSeed.forEach { month ->
                val yearMonth = YearMonth.of(currentYear, month)
                val daysInMonth = yearMonth.lengthOfMonth()

                for (day in 1..daysInMonth) {
                    val localDate = LocalDate.of(currentYear, month, day)
                    val createAt = localDate.atStartOfDay(zoneId).toInstant().toEpochMilli()

                    val steps = Random.nextInt(4000, 12001)
                    val stepGoal = 10000
                    val spentTime = steps * 1L
                    val calories = (steps * 0.04).toInt()
                    val distance = round(steps * 0.0007 * 100) / 100.0

                    val entity = DailyStepEntity(
                        createAt = createAt,
                        steps = steps,
                        stepGoal = stepGoal,
                        spentTime = spentTime,
                        calories = calories,
                        distance = distance
                    )
                    dailyStepsDao.upsert(entity)
                }
            }
        }
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