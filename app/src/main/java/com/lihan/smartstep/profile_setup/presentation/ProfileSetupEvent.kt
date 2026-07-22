package com.lihan.smartstep.profile_setup.presentation

sealed interface ProfileSetupEvent {
    data object OnNavigateToDashboard: ProfileSetupEvent
}