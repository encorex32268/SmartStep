package com.lihan.smartstep.stepcount.presentation

sealed interface SmartStepAction {
    data object OnStepGoalClick: SmartStepAction
    data object OnPersonSettingsClick: SmartStepAction
    data object OnExitClick: SmartStepAction
    data object OnMenuClick: SmartStepAction
    data object OnStepGoalSaveClick: SmartStepAction
    data object OnDismissStepGoal: SmartStepAction
    data class OnStepGoalValueChanged(val value: String): SmartStepAction
}