package com.lihan.smartstep.coach.presentation

import androidx.compose.foundation.text.input.TextFieldState
import com.lihan.smartstep.coach.presentation.model.MessageUi

data class AICoachState(
    val isLoading: Boolean = false,
    val items: List<MessageUi> = emptyList(),
    val isAiThinking: Boolean = false,
    val isExpandSuggestion: Boolean = false,
    val suggestionTextField: TextFieldState = TextFieldState()
){
    val isTextFieldEnabled: Boolean
        get() = !isLoading && !isAiThinking
}
