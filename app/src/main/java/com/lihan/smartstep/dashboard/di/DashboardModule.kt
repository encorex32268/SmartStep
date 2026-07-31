package com.lihan.smartstep.dashboard.di

import com.lihan.smartstep.dashboard.data.GeminiAICoach
import com.lihan.smartstep.dashboard.domain.AICoach
import com.lihan.smartstep.dashboard.presentation.DashboardViewModel
import com.lihan.smartstep.dashboard.presentation.aicoach.AICoachViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dashboardModule = module {

    singleOf(::GeminiAICoach).bind<AICoach>()

    viewModelOf(::DashboardViewModel)
    viewModelOf(::AICoachViewModel)
}