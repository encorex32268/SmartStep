package com.lihan.smartstep.stepcount.data.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.lihan.smartstep.stepcount.domain.DailySyncScheduler
import com.lihan.smartstep.stepcount.domain.model.DailyStep
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit

class DailySyncWorkerScheduler(
    private val context: Context
): DailySyncScheduler {

    override fun triggerSync(dailyStep: DailyStep) {

        val delay = calculateDelayUntilMidnight()
        val request = PeriodicWorkRequestBuilder<DailySyncWorker>(
            24, TimeUnit.HOURS
        )
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(
                workDataOf(
                    DailySyncWorker.TIMESTAMP to dailyStep.timestamp,
                    DailySyncWorker.GOAL_STEPS to dailyStep.goal,
                    DailySyncWorker.STEPS to dailyStep.steps
                )
            )
            .addTag("daily_sync")
            .build()

        WorkManager.getInstance(context)
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