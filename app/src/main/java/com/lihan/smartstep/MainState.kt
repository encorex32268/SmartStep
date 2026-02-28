package com.lihan.smartstep

data class MainState(
    val isReady: Boolean = false,
    val startDestination: Route = Route.OnboardingProfileSetting
)
