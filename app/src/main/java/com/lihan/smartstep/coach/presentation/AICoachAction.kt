package com.lihan.smartstep.coach.presentation

sealed interface AICoachAction {
    data object OnBackClick: AICoachAction
    data object OnSendClick: AICoachAction
    data class OnSuggestionClick(val suggestion: String): AICoachAction
    data object OnQuickSuggestionClick: AICoachAction
}