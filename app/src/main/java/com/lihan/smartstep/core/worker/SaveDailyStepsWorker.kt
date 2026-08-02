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
import java.time.format.DateTimeFormatter

class SaveDailyStepsWorker(
    val appContext: Context,
    workerParameters: WorkerParameters,
    private val stepsRepository: DailyStepsRepository,
    private val getStepMetricsUseCase: GetStepMetricsUseCase,
    private val userDataStore: UserDataStore,
): CoroutineWorker(appContext, workerParameters){

    override suspend fun doWork(): Result {

        val createAt = LocalDate.now()
            .minusDays(1)
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
            val timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            file.appendText("[$timestamp] $message\n")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}