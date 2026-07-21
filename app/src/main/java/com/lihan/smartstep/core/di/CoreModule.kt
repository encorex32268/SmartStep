package com.lihan.smartstep.core.di

import com.lihan.smartstep.core.data.DefaultUserDataStore
import com.lihan.smartstep.core.domain.UserDataStore
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreModule = module {

    singleOf(::DefaultUserDataStore).bind<UserDataStore>()
}