package com.lihan.smartstep.stepcount.presentation

import java.text.DecimalFormat

data class SmartStepState(
    val step: Long = 4523,
    val totalStep: Long = 6000,
    val isShowStepGoal: Boolean = false,
    val motionSensorsPermissionGranted: Boolean = false,
    val isShowSensorsModal: Boolean = false,
    val isShowEnableAccessModal: Boolean = false,
    val isShowBackgroundAccessModal: Boolean = false,
    val hasRequestPermission: Boolean = false,
    val isShowExitModal: Boolean = false,
)

val stepGoalItems = (1..40).map { (it * 1000) .toString()}


fun Long.formatThousands(): String {
    return DecimalFormat("#,###").format(this)
}