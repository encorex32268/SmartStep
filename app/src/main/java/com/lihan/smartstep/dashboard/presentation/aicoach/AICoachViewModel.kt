package com.lihan.smartstep.dashboard.presentation.aicoach

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lihan.smartstep.dashboard.domain.AICoach
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AICoachViewModel(
    private val aiCoach: AICoach
): ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(AICoachState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {

                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = AICoachState()
        )

    fun onAction(action: AICoachAction) {
        when (action) {
            else -> Unit
        }
    }

}