package com.lihan.smartstep.stepcount.presentation

import androidx.compose.foundation.text.input.TextFieldState
import java.text.DecimalFormat

data class SmartStepState(
    val step: Long = 0,
    val totalStep: Long = 6000,
    val isShowStepGoal: Boolean = false,
    val motionSensorsPermissionGranted: Boolean = false,
    val isShowSensorsModal: Boolean = false,
    val isShowEnableAccessModal: Boolean = false,
    val isShowBackgroundAccessModal: Boolean = false,
    val hasRequestPermission: Boolean = false,
    val isShowExitModal: Boolean = false,
    val isShownBackgroundAccessModal: Boolean = false,
    val isShowEditSteps: Boolean = false,
    val isShowResetStepsDialog: Boolean = false,
    val isShowDatePicker: Boolean = false,
    val editStepsDateTextFieldState: TextFieldState = TextFieldState(),
    val editStepsStepsTextFieldState: TextFieldState = TextFieldState(),
    val isTrackingStep: Boolean = false
)

val stepGoalItems = (1..40).map { (it * 1000) .toString()}


fun Long.formatThousands(): String {
    return DecimalFormat("#,###").format(this)
}