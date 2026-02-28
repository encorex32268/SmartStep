package com.lihan.smartstep.stepcount.di

import com.lihan.smartstep.stepcount.presentation.SmartStepViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val stepCountModule = module {
    viewModelOf(::SmartStepViewModel)
}