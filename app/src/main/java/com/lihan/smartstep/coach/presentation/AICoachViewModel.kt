package com.lihan.smartstep.coach.presentation

import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lihan.smartstep.coach.domain.AIGenerator
import com.lihan.smartstep.coach.presentation.model.MessageUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AICoachViewModel(
    private val aiGenerator: AIGenerator
): ViewModel(){

    private var hasLoadedInitialDate = false

    private val _state = MutableStateFlow(AICoachState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialDate){
                greeting()
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
        viewModelScope.launch {
            val message = state.value.suggestionTextField.text.toString()
            _state.update { it.copy(
                items = it.items + MessageUi(isUser = true , content = message),
                isAiThinking = true,
                isExpandSuggestion = false
            ) }
            state.value.suggestionTextField.clearText()

            val result = aiGenerator.sendSuggestion(message)
            val newItems = if (result == null){
                state.value.items
            }else{
                state.value.items + MessageUi(isUser = false, content = result  )
            }
            _state.update { it.copy(
                isAiThinking = false,
                items = newItems
            ) }
        }
    }

    private fun greeting(){
        _state.update { it.copy(
            items = listOf(
                MessageUi(
                    isUser = false,
                    content = "Hello! I'm your AI fitness coach. I've noticed your activity levels are a bit lower than usual today. I'm here to help you get back on track and answer any questions you might have about your fitness journey."
                )
            )
        ) }
    }
}