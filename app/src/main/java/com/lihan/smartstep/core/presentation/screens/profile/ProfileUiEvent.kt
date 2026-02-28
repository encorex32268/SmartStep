package com.lihan.smartstep.core.presentation.screens.profile

sealed interface ProfileUiEvent {
    data object OnNavigateToSmartStep: ProfileUiEvent
}