package com.lihan.smartstep.coach.di

import com.lihan.smartstep.coach.data.GeminiAIGenerator
import com.lihan.smartstep.coach.domain.AIGenerator
import com.lihan.smartstep.coach.presentation.AICoachViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coachModule = module {

    single { GeminiAIGenerator(
        smartStepRepository = get(),
        appDataStore = get()
    ) }.bind<AIGenerator>()

    viewModelOf(::AICoachViewModel)
}