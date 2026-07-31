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
import java.time.LocalDate
import java.time.ZoneId

class SaveDailyStepsWorker(
    val appContext: Context,
    workerParameters: WorkerParameters,
    private val stepsRepository: DailyStepsRepository,
    private val getStepMetricsUseCase: GetStepMetricsUseCase,
    private val userDataStore: UserDataStore,
): CoroutineWorker(appContext,workerParameters){

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
            Result.success()
        }catch (e: Exception){
            e.printStackTrace()
//            saveToFile(dailyStep)
            Log.d(SaveDailyStepsScheduler.WORK_NAME, "doWork: retry")
            Result.retry()
        }
    }


//    private fun saveToFile(dailyStep: DailyStep){
//        val fileDir = File(appContext.filesDir,"fallback_logs").apply {
//            if (!exists()) mkdirs()
//        }
//        val file = File(fileDir,"fallback.txt")
//        val text = Json.encodeToString(dailyStep)
//        file.appendText(text)
//    }
}