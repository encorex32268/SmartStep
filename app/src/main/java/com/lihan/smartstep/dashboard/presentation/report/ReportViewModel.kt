package com.lihan.smartstep.dashboard.presentation.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class ReportViewModel : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(ReportState())
    val state = _state.onStart {
        if (!hasLoadedInitialData) {
            /** init */
            hasLoadedInitialData = true
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ReportState()
        )

    fun onAction(action: ReportAction) {
        when (action) {
            else -> Unit
        }
    }
}