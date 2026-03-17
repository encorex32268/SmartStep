package com.lihan.smartstep.stepcount.presentation

sealed interface SmartStepUiEvent {
    data object OnExitApp: SmartStepUiEvent
}