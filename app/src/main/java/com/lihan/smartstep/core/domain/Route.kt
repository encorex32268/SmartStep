package com.lihan.smartstep.core.domain

import kotlinx.serialization.Serializable


sealed interface Route{

    @Serializable
    data object OnboardingProfileSetting: Route

    @Serializable
    data object SmartStep: Route

    @Serializable
    data object PersonalSettings: Route

    @Serializable
    data object Test: Route

    @Serializable
    data object AICoach: Route

}