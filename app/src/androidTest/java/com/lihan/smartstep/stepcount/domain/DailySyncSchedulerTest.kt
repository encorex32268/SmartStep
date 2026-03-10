package com.lihan.smartstep.stepcount.domain

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.work.Configuration
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import androidx.work.testing.SynchronousExecutor
import androidx.work.testing.TestListenableWorkerBuilder
import androidx.work.testing.WorkManagerTestInitHelper
import androidx.work.workDataOf
import com.lihan.smartstep.core.data.AppUserInfo
import com.lihan.smartstep.core.domain.UserInfoDataStore
import com.lihan.smartstep.stepcount.data.local.SmartStepDatabase
import com.lihan.smartstep.stepcount.data.repository.SmartStepRepositoryImpl
import com.lihan.smartstep.core.data.worker.DailySyncWorker
import com.lihan.smartstep.core.data.worker.DailySyncWorkerScheduler
import com.lihan.smartstep.core.domain.DailySyncScheduler
import com.lihan.smartstep.stepcount.domain.repository.SmartStepRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DailySyncSchedulerTest {

    private lateinit var context: Context
    private lateinit var scheduler: DailySyncScheduler
    private lateinit var smartStepRepository: SmartStepRepository
    private lateinit var database: SmartStepDatabase
    private lateinit var userInfoDataStore: UserInfoDataStore


    @Before
    fun setup() {
        context = InstrumentationRegistry.getInstrumentation().targetContext

        val config = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setExecutor(SynchronousExecutor())
            .build()
        WorkManagerTestInitHelper.initializeTestWorkManager(context, config)

        scheduler = DailySyncWorkerScheduler(context)

        userInfoDataStore = AppUserInfo(context)

        database = Room.inMemoryDatabaseBuilder(
            context, SmartStepDatabase::class.java
        ).allowMainThreadQueries().build()

        smartStepRepository = SmartStepRepositoryImpl(
            database = database,
            userInfoDataStore = userInfoDataStore
        )
    }

    @Test
    fun testTriggerSync_enqueuesCorrectData() = runBlocking {

        val testTimestamp = 123456789L
        val testGoal = 10000L
        val testSteps = 5000L

        val inputData = workDataOf(
            DailySyncWorker.TIMESTAMP to testTimestamp,
            DailySyncWorker.GOAL_STEPS to testGoal,
            DailySyncWorker.STEPS to testSteps
        )

        val myFactory = object : WorkerFactory() {
            override fun createWorker(
                appContext: Context,
                workerClassName: String,
                workerParameters: WorkerParameters
            ): ListenableWorker {
                return DailySyncWorker(appContext, workerParameters, database)
            }
        }

        val worker = TestListenableWorkerBuilder<DailySyncWorker>(context)
            .setInputData(inputData)
            .setWorkerFactory(myFactory)
            .build()


        val result = worker.doWork()

//        val data  = database.dailyStepDao.getDailyStep(timestamp = testTimestamp).first()
//
//        assertEquals(ListenableWorker.Result.success(), result)
//        assertEquals(data.timestamp, testTimestamp)
//        assertEquals(data.goal, testGoal)
//        assertEquals(data.steps, testSteps)

    }
}