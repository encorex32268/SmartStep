package com.lihan.smartstep.dashboard.presentation.aicoach

sealed interface AICoachAction {
    data object OnBackClick: AICoachAction
}