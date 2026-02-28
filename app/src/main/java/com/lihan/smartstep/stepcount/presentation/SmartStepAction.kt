package com.lihan.smartstep.stepcount.presentation

sealed interface SmartStepAction {
    data object OnStepGoalClick: SmartStepAction
    data object OnPersonSettingsClick: SmartStepAction
    data object OnExitClick: SmartStepAction
    data object OnMenuClick: SmartStepAction
    data class OnStepGoalSaveClick(val value: String): SmartStepAction
    data object OnDismissStepGoal: SmartStepAction
    data class OnUpdatePermission(
        val isGranted: Boolean
    ): SmartStepAction
    data object OnShowSensorsAccessModal: SmartStepAction
    data object OnShowEnableAccessModal: SmartStepAction
    data object OnShowBackgroundAccessModal: SmartStepAction
    data object OnShowBackgroundAccessModalFirstTime: SmartStepAction
    data object OnDismissSensorsAccessModal: SmartStepAction
    data object OnDismissEnableAccessModal: SmartStepAction
    data object OnDismissBackgroundAccessModal: SmartStepAction
    data object OnResumeGetGranted: SmartStepAction
    data object OnDismissExitModal: SmartStepAction
    data object OnShowExitModal: SmartStepAction
    data object OnPermissionGranted: SmartStepAction
}