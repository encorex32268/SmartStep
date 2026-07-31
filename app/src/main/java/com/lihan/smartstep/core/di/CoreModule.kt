package com.lihan.smartstep.core.di

import androidx.room.Room
import com.lihan.smartstep.core.data.DefaultUserDataStore
import com.lihan.smartstep.core.data.LocalDailyStepsRepository
import com.lihan.smartstep.core.data.StepsSensorManager
import com.lihan.smartstep.core.database.DailyStepsDao
import com.lihan.smartstep.core.database.SmartStepDatabase
import com.lihan.smartstep.core.domain.AppSensorManager
import com.lihan.smartstep.core.domain.DailyStepsRepository
import com.lihan.smartstep.core.domain.UserDataStore
import com.lihan.smartstep.core.domain.usecase.GetStepMetricsUseCase
import com.lihan.smartstep.dashboard.data.DefaultAppPowerManager
import com.lihan.smartstep.dashboard.domain.AppPowerManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import com.lihan.smartstep.core.worker.SaveDailyStepsWorker
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreModule = module {
    single {
        CoroutineScope(SupervisorJob() + Dispatchers.IO)
    }
    singleOf(::DefaultUserDataStore).bind<UserDataStore>()
    singleOf(::DefaultAppPowerManager).bind<AppPowerManager>()
    singleOf(::StepsSensorManager).bind<AppSensorManager>()

    single {
        Room.databaseBuilder(
            context = androidContext(),
            klass = SmartStepDatabase::class.java,
            name = "smartstep.db"
        ).build()
    }

    single { get<SmartStepDatabase>().dailyStepDao() }.bind<DailyStepsDao>()

    singleOf(::LocalDailyStepsRepository).bind<DailyStepsRepository>()
    singleOf(::GetStepMetricsUseCase)
    workerOf(::SaveDailyStepsWorker)
}