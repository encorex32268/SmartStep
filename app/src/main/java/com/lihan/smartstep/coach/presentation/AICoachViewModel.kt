package com.lihan.smartstep.coach.presentation

import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AICoachViewModel : ViewModel(){

    private var hasLoadedInitialDate = false

    private val _state = MutableStateFlow(AICoachState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialDate){
                loadMessages()
                hasLoadedInitialDate = true
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            AICoachState()
        )

    fun onAction(action: AICoachAction){
        when(action){
            AICoachAction.OnBackClick -> Unit
            AICoachAction.OnQuickSuggestionClick -> quickSuggestionClick()
            AICoachAction.OnSendClick -> sendMessageToAI()
            is AICoachAction.OnSuggestionClick -> suggestionClick(action.suggestion)

        }
    }


    private fun quickSuggestionClick(){
        _state.update { it.copy(
            isExpandSuggestion = !it.isExpandSuggestion
        )}
    }
    private fun suggestionClick(suggestion: String) {
        state.value.suggestionTextField.setTextAndPlaceCursorAtEnd(suggestion)
    }

    private fun sendMessageToAI(){
        //TODO: Gemini API
    }

    private fun loadMessages(){
        //TODO: Reload messages from database
        viewModelScope.launch {

        }
    }
}