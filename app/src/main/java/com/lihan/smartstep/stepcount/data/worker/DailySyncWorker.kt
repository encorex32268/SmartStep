package com.lihan.smartstep.stepcount.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.lihan.smartstep.stepcount.data.local.DailyStepEntity
import com.lihan.smartstep.stepcount.data.local.SmartStepDatabase

class DailySyncWorker(
    appContext: Context,
    val workerParameters: WorkerParameters,
    val database: SmartStepDatabase
): CoroutineWorker(
    appContext,
    workerParameters
){
    companion object {
        const val TIMESTAMP = "timestamp"
        const val GOAL_STEPS = "goal_steps"
        const val STEPS = "steps"
    }

    override suspend fun doWork(): Result {
        if (runAttemptCount > 3){
            return Result.failure()
        }
        val timestamp = workerParameters.inputData.getLong(TIMESTAMP,0L)
        val goal = workerParameters.inputData.getLong(GOAL_STEPS,0L)
        val steps = workerParameters.inputData.getLong(STEPS,0L)

        val entity =  DailyStepEntity(
            timestamp = timestamp,
            goal = goal,
            steps = steps
        )

        return try {
            database.dailyStepDao.upsert(entity)
            Result.success()
        }catch (e: Exception){
            e.printStackTrace()
            Result.retry()
        }

    }

}