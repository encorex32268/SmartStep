package com.lihan.smartstep.dashboard.presentation.aicoach

sealed interface AICoachAction {
    data class OnGreetingFromAI(val greeting: String): AICoachAction
    data object OnBackClick: AICoachAction
    data object OnSendClick: AICoachAction
    data class OnSelectedSuggestion(val suggestion: String): AICoachAction
    data object OnSuggestionClick: AICoachAction
}