package com.lihan.smartstep.stepcount.di

import androidx.room.Room
import com.lihan.smartstep.core.data.datastore.DefaultAppDataStore
import com.lihan.smartstep.core.domain.AppDataStore
import com.lihan.smartstep.stepcount.data.SmartStepTracker
import com.lihan.smartstep.core.data.datastore.local.DailyStepDao
import com.lihan.smartstep.core.data.datastore.local.SmartStepDatabase
import com.lihan.smartstep.stepcount.data.repository.SmartStepRepositoryImpl
import com.lihan.smartstep.stepcount.data.sensor.DefaultSensorManager
import com.lihan.smartstep.stepcount.data.worker.DailySyncWorkerScheduler
import com.lihan.smartstep.stepcount.domain.repository.SmartStepRepository
import com.lihan.smartstep.stepcount.domain.sensor.StepSensorManager
import com.lihan.smartstep.stepcount.domain.worker.DailySyncScheduler
import com.lihan.smartstep.stepcount.presentation.SmartStepViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val stepCountModule = module {
    viewModelOf(::SmartStepViewModel)

    single {
        Room.databaseBuilder(
            context = androidContext(),
            SmartStepDatabase::class.java,
            "smart_step.db"
        ).build()
    }

    single { get<SmartStepDatabase>().dailyStepDao }.bind<DailyStepDao>()
    single {
        DefaultSensorManager(
            context = androidContext(),
            appDataStore = get()
        )
    }.bind<StepSensorManager>()

    single { SmartStepRepositoryImpl(
        database = get()
    ) }.bind<SmartStepRepository>()

    singleOf(::DefaultAppDataStore).bind<AppDataStore>()
    singleOf(::DailySyncWorkerScheduler).bind<DailySyncScheduler>()

    single{
        SmartStepTracker(
            applicationScope = get(),
            applicationContext = androidApplication().applicationContext,
            appDataStore = get(),
            userStepSensorManager = get(),
            logger = get()
        )
    }

}