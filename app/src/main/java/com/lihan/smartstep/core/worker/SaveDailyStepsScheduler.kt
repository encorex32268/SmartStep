package com.lihan.smartstep.core.worker

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.time.Duration
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

object SaveDailyStepsScheduler {

     const val WORK_NAME = "DailyWork"

    fun scheduleWork(context: Context) {

        val initialDelay = calculateDelayToMidnight()

        val dailyWorkRequest = PeriodicWorkRequestBuilder<SaveDailyStepsWorker>(
            24, TimeUnit.HOURS
        )
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            dailyWorkRequest
        )
    }


    private fun calculateDelayToMidnight(): Long {
        val now = LocalDateTime.now()
        val nextMidnight = now.toLocalDate().plusDays(1).atStartOfDay()

        return Duration.between(now, nextMidnight).toMillis()
    }
}