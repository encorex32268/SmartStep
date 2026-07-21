package com.lihan.smartstep.profile_setup.di

import com.lihan.smartstep.profile_setup.presentation.ProfileSetupViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val profileSetupModule = module {

    viewModelOf(::ProfileSetupViewModel)
}