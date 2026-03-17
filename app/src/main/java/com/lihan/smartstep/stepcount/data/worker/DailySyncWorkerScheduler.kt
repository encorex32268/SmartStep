package com.lihan.smartstep.stepcount.data.worker

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.lihan.smartstep.stepcount.domain.worker.DailySyncScheduler
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit

class DailySyncWorkerScheduler(
    private val context: Context
): DailySyncScheduler {

    override suspend fun triggerSync() {

        val workManager = WorkManager.getInstance(context)

        val delay = calculateDelayUntilMidnight()
        val request = PeriodicWorkRequestBuilder<DailySyncWorker>(
            24, TimeUnit.HOURS
        )
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .addTag("daily_sync")
            .build()

        workManager
            .enqueueUniquePeriodicWork(
                "MidnightSync",
                ExistingPeriodicWorkPolicy.UPDATE,
                request
        )



    }

    private fun calculateDelayUntilMidnight(): Long {
        val zoneId = ZoneId.systemDefault()
        val now = LocalDateTime.now().atZone(zoneId).toInstant().toEpochMilli()
        val midnight = LocalDateTime.now().plusDays(1).atZone(zoneId).toInstant().toEpochMilli()
        return midnight - now
    }
}