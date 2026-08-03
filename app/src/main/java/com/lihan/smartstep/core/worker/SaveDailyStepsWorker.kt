package com.lihan.smartstep.core.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.lihan.smartstep.core.domain.DailyStepsRepository
import com.lihan.smartstep.core.domain.UserDataStore
import com.lihan.smartstep.core.domain.model.DailyStep
import com.lihan.smartstep.core.domain.usecase.GetStepMetricsUseCase
import kotlinx.coroutines.flow.first
import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class SaveDailyStepsWorker(
    val appContext: Context,
    workerParameters: WorkerParameters,
    private val stepsRepository: DailyStepsRepository,
    private val getStepMetricsUseCase: GetStepMetricsUseCase,
    private val userDataStore: UserDataStore,
): CoroutineWorker(appContext, workerParameters){

    override suspend fun doWork(): Result {
        val now = ZonedDateTime.now(ZoneId.systemDefault())

        // WorkManager 可能在午夜前 (例如 20:00~23:59) 提前被喚醒，
        // 此時當天的步數屬於 TODAY (now.toLocalDate())。
        // 若在跨日後 (例如 00:00~12:00) 被觸發，則紀錄屬於 YESTERDAY (now.toLocalDate().minusDays(1))。
        val targetDate = if (now.hour >= 20) {
            now.toLocalDate()
        } else {
            now.toLocalDate().minusDays(1)
        }

        val createAt = targetDate
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        val stepMetrics = getStepMetricsUseCase().first()
        val trackingTime = userDataStore.trackingTime.first()

        val dailyStep = DailyStep(
            createAt = createAt,
            steps = stepMetrics.steps,
            stepsGoal = stepMetrics.stepGoal,
            spentTime = trackingTime
        )

        Log.d(SaveDailyStepsScheduler.WORK_NAME, "doWork: dailyStep: $dailyStep")
        return try {
            stepsRepository.upsert(dailyStep)

            userDataStore.cleanStepsData()
            Log.d(SaveDailyStepsScheduler.WORK_NAME, "doWork: success")
            writeTimestampToFile("Execution SUCCESS: $dailyStep")
            Result.success()
        } catch (e: Exception){
            e.printStackTrace()
            Log.d(SaveDailyStepsScheduler.WORK_NAME, "doWork: retry")
            writeTimestampToFile("Execution FAILED: ${e.message}")
            Result.retry()
        }
    }

    private fun writeTimestampToFile(message: String) {
        try {
            val fileDir = appContext.filesDir
            val file = File(fileDir, "daily_steps_log.txt")
            val timestamp = ZonedDateTime.now(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
            file.appendText("[$timestamp] $message\n")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}