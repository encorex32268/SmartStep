package com.lihan.smartstep.stepcount.di

import androidx.room.Room
import com.lihan.smartstep.stepcount.data.DefaultSensorManager
import com.lihan.smartstep.stepcount.data.local.DailyStepDao
import com.lihan.smartstep.stepcount.data.local.SmartStepDatabase
import com.lihan.smartstep.stepcount.data.repository.SmartStepRepositoryImpl
import com.lihan.smartstep.stepcount.domain.AppSensorManager
import com.lihan.smartstep.stepcount.domain.repository.SmartStepRepository
import com.lihan.smartstep.stepcount.presentation.SmartStepViewModel
import org.koin.android.ext.koin.androidContext
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
            userInfoDataStore = get()
        )
    }.bind<AppSensorManager>()

    single { SmartStepRepositoryImpl(
        database = get()
    ) }.bind<SmartStepRepository>()
}