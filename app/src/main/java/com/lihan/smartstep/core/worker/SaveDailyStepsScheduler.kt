package com.lihan.smartstep.core.worker

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit

object SaveDailyStepsScheduler {

    const val WORK_NAME = "DailyWork"

    fun scheduleWork(context: Context) {

        val initialDelay = calculateMidnight()

        val dailyWorkRequest = PeriodicWorkRequestBuilder<SaveDailyStepsWorker>(
            24, TimeUnit.HOURS
        ).setInitialDelay(initialDelay).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            dailyWorkRequest
        )
    }


    private fun calculateMidnight(): java.time.Duration {
        val zone = ZoneId.systemDefault()
        val now = ZonedDateTime.now(zone)
        val nextMidnight = now.toLocalDate().plusDays(1).atStartOfDay(zone)

        return java.time.Duration.between(now, nextMidnight)
    }
}