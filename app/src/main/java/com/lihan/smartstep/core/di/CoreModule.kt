package com.lihan.smartstep.core.di

import com.lihan.smartstep.core.data.DefaultUserDataStore
import com.lihan.smartstep.core.data.StepsSensorManager
import com.lihan.smartstep.core.domain.AppSensorManager
import com.lihan.smartstep.core.domain.UserDataStore
import com.lihan.smartstep.dashboard.data.DefaultAppPowerManager
import com.lihan.smartstep.dashboard.domain.AppPowerManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
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
}