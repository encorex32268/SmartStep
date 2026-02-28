package com.lihan.smartstep.core.di

import com.lihan.smartstep.MainViewModel
import com.lihan.smartstep.core.data.AppUserInfo
import com.lihan.smartstep.core.domain.UserInfoDataStore
import com.lihan.smartstep.core.presentation.screens.profile.ProfileViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreModule = module {
    single{
        AppUserInfo(context = androidContext())
    }.bind<UserInfoDataStore>()

    viewModelOf(::ProfileViewModel)
    viewModelOf(::MainViewModel)
}