package com.lihan.smartstep.core.di

import com.lihan.smartstep.MainViewModel
import com.lihan.smartstep.SmartStepApplication
import com.lihan.smartstep.core.data.AppUserInfo
import com.lihan.smartstep.core.data.SmartStepTracker
import com.lihan.smartstep.core.data.worker.DailySyncWorkerScheduler
import com.lihan.smartstep.core.domain.DailySyncScheduler
import com.lihan.smartstep.core.domain.UserInfoDataStore
import com.lihan.smartstep.core.presentation.screens.profile.ProfileViewModel
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreModule = module {

    single<CoroutineScope>{
        (androidApplication() as SmartStepApplication).applicationScope
    }

    singleOf(::AppUserInfo).bind<UserInfoDataStore>()
    singleOf(::DailySyncWorkerScheduler).bind<DailySyncScheduler>()

    single{
        SmartStepTracker(
            applicationScope = get(),
            applicationContext = androidApplication().applicationContext,
            userInfoDataStore = get(),
            appSensorManager = get(),
            repository = get()
        )
    }

    viewModelOf(::ProfileViewModel)
    viewModelOf(::MainViewModel)

}