package com.lihan.smartstep.stepcount.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.lihan.smartstep.core.domain.AppDataStore
import com.lihan.smartstep.stepcount.data.SmartStepTracker
import com.lihan.smartstep.stepcount.data.local.DailyStepEntity
import com.lihan.smartstep.stepcount.data.local.SmartStepDatabase
import com.lihan.smartstep.stepcount.presentation.utils.DateTimeUtils
import kotlinx.coroutines.flow.first

class DailySyncWorker(
    appContext: Context,
    val workerParameters: WorkerParameters,
    val database: SmartStepDatabase,
    val appDataStore: AppDataStore,
    val smartStepTracker: SmartStepTracker
): CoroutineWorker(
    appContext,
    workerParameters
){

    override suspend fun doWork(): Result {
        if (runAttemptCount > 3){
            return Result.failure()
        }

        val stepsDate = smartStepTracker.stepDate.first()
        val goal = appDataStore.getTotalStep().first()

        val steps = stepsDate.steps
        val time = stepsDate.countingTimestamp

        val entity =  DailyStepEntity(
            goal = goal,
            steps = steps,
            time = time,
            dayTimestamp = DateTimeUtils.getTodayEpochMilli()
        )

        return try {
            database.dailyStepDao.upsert(entity)
            appDataStore.reset()
            smartStepTracker.reset()
            Result.success()
        }catch (e: Exception){
            e.printStackTrace()
            Result.retry()
        }

    }

}