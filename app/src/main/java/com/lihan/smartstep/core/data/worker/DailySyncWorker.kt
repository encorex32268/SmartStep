package com.lihan.smartstep.core.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.lihan.smartstep.core.domain.UserInfoDataStore
import com.lihan.smartstep.stepcount.data.local.DailyStepEntity
import com.lihan.smartstep.stepcount.data.local.SmartStepDatabase
import com.lihan.smartstep.stepcount.presentation.utils.DateTimeUtils
import kotlinx.coroutines.flow.first

class DailySyncWorker(
    appContext: Context,
    val workerParameters: WorkerParameters,
    val database: SmartStepDatabase,
    val userInfoDataStore: UserInfoDataStore
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

        val goal = userInfoDataStore.getTotalStep().first()
        val steps = userInfoDataStore.getTodaySteps().first()

        val entity =  DailyStepEntity(
            timestamp = DateTimeUtils.getTodayEpochMilli(),
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