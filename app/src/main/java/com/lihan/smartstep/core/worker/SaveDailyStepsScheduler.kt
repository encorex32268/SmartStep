package com.lihan.smartstep.core.worker

import android.content.Context
import android.util.Log
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.lihan.smartstep.core.receiver.AndroidAlarmManager
import com.lihan.smartstep.core.receiver.AppAlarmManager
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit

object SaveDailyStepsScheduler {

    private const val TAG = "SaveDailyStepsScheduler"
    const val WORK_NAME = "DailyWork"
    const val ONE_TIME_WORK_NAME = "DailyWork_OneTime"

    /**
     * 啟動排程策略：
     * 1. 優先嘗試設定 Exact Alarm (在 00:01 觸發)。
     *    - 若成功：取消 WorkManager 的週期性備援任務，等待 ExactAlarmReceiver 在指定時間觸發 Worker。
     * 2. 若 Alarm 設置失敗 (如 Android 12+ 未取得權限或拋出例外)：
     *    - 繼續/降級使用 WorkManager 的週期性任務作為備援。
     */
    fun schedule(
        context: Context,
        alarmManager: AppAlarmManager = AndroidAlarmManager(context)
    ) {
        val isAlarmSet = alarmManager.setupBackupAlarm()
        if (isAlarmSet) {
            Log.d(TAG, "Exact alarm scheduled successfully. Cancelling fallback periodic worker.")
            cancelPeriodicWork(context)
        } else {
            Log.w(TAG, "Exact alarm failed to schedule. Falling back to periodic worker.")
            schedulePeriodicWork(context)
        }
    }

    /**
     * 當 Alarm 在指定時間觸發時，立即以 OneTimeWorkRequest 執行 Worker
     */
    fun executeNow(context: Context) {
        Log.d(TAG, "Executing SaveDailyStepsWorker immediately via WorkManager.")
        val immediateWorkRequest = OneTimeWorkRequestBuilder<SaveDailyStepsWorker>()
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            ONE_TIME_WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            immediateWorkRequest
        )
    }

    /**
     * 排程 WorkManager 週期性備援任務
     */
    fun schedulePeriodicWork(context: Context) {
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

    /**
     * 取消 WorkManager 週期性任務
     */
    fun cancelPeriodicWork(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
    }

    private fun calculateMidnight(): java.time.Duration {
        val zone = ZoneId.systemDefault()
        val now = ZonedDateTime.now(zone)
        val nextMidnight = now.toLocalDate().plusDays(1).atStartOfDay(zone)

        return java.time.Duration.between(now, nextMidnight)
    }
}