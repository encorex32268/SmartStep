package com.lihan.smartstep

import com.lihan.smartstep.core.domain.Route

data class MainState(
    val isReady: Boolean = false,
    val startDestination: Route = Route.OnboardingProfileSetting
)
