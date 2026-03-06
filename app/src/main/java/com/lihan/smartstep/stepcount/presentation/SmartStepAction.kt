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
    data object OnExitOkClick: SmartStepAction
    data object OnPermissionGranted: SmartStepAction
    data object OnEditStepsClick: SmartStepAction
    data object OnResetTodayStepsClick: SmartStepAction
    data object OnResetStepClick: SmartStepAction
    data object OnDismissResetStepsDialog: SmartStepAction
    data object OnDismissDatePickerDialog: SmartStepAction
    data object OnDismissEditStepsDialog: SmartStepAction
    data object OnEditStepsSaveClick: SmartStepAction
    data object OnShowDatePicker: SmartStepAction
    data object OnDismissDatePicker: SmartStepAction
    data class OnDatePickerSaveClick(val timestamp: Long): SmartStepAction
}