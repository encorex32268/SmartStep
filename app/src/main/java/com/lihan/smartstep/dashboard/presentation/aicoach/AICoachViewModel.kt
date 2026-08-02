package com.lihan.smartstep.dashboard.presentation.aicoach

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lihan.smartstep.core.domain.usecase.GetStepMetricsUseCase
import com.lihan.smartstep.dashboard.domain.AICoach
import com.lihan.smartstep.dashboard.domain.Message
import com.lihan.smartstep.dashboard.presentation.aicoach.components.Sender
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AICoachViewModel(
    private val aiCoach: AICoach,
    private val getStepMetricsUseCase: GetStepMetricsUseCase
): ViewModel() {


    private val _state = MutableStateFlow(AICoachState())
    val state = _state
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = AICoachState()
        )


    fun onAction(action: AICoachAction) {
        when (action) {
            AICoachAction.OnBackClick -> Unit
            is AICoachAction.OnSelectedSuggestion -> selectedSuggestion(action.suggestion)
            AICoachAction.OnSendClick -> sendMessage()
            AICoachAction.OnSuggestionClick -> suggestionClick()
            is AICoachAction.OnGreetingFromAI -> greetingFromAI(action.greeting)
        }
    }

    private fun greetingFromAI(greeting: String){
        _state.update { it.copy(
            messages = listOf(Message(sender = Sender.AI, message = greeting))
        ) }
    }

    private fun sendMessage(){
        sendMessageToAICoach(
            message = state.value.messageTextFieldState.text.toString()
        )
    }
    private fun suggestionClick(){
        _state.update { it.copy(
            isShowSuggestions = !it.isShowSuggestions
        )}
    }
    private fun selectedSuggestion(suggestion: String){
        _state.update { it.copy(
            isShowSuggestions = false
        ) }
        sendMessageToAICoach(message = suggestion)
    }

    private fun sendMessageToAICoach(message: String){
        viewModelScope.launch {
            _state.update { it.copy(
                messages = it.messages + Message(sender = Sender.User, message = message),
                isThinking = true
            ) }
            val stepMetrics = getStepMetricsUseCase().first()
            val currentSteps = stepMetrics.steps
            val stepGoal = stepMetrics.stepGoal
            val distanceKm = stepMetrics.distance
            val caloriesBurned = stepMetrics.kcal
            val result = aiCoach
                .generateCoachResponseWithStepData(
                    userMessage = message,
                    currentSteps = currentSteps,
                    stepGoal = stepGoal,
                    spentTimeMinutes = 0,
                    distanceKm = distanceKm,
                    caloriesBurned = caloriesBurned
                )

            _state.update { it.copy(
                messages = it.messages + listOf(Message(sender = Sender.AI, message = result)),
                isThinking = false
            )}
        }

    }

}