package com.lihan.smartstep.core.worker

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit
import kotlin.time.Duration

object SaveDailyStepsScheduler {

    const val WORK_NAME = "DailyWork"

    fun scheduleWork(context: Context) {

        val initialDelay = calculateMidnight()

        val dailyWorkRequest = PeriodicWorkRequestBuilder<SaveDailyStepsWorker>(
            24, TimeUnit.HOURS
        ).setInitialDelay(initialDelay).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            dailyWorkRequest
        )
    }


    private fun calculateMidnight(): java.time.Duration {
        val now = LocalDateTime.now()
        val nextMidnight = LocalDate.now().plusDays(1).atStartOfDay()

        return java.time.Duration.between(now, nextMidnight)
    }
}