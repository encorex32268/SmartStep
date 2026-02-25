package com.lihan.smartstep.stepcount.presentation

import androidx.compose.material3.DrawerValue
import java.text.DecimalFormat

data class SmartStepState(
    val step: Long = 4523,
    val totalStep: Long = 6000,
    val isShowStepGoal: Boolean = false
)

val stepGoalItems = (1..40).map { (it * 1000) .toString()}


fun Long.formatThousands(): String {
    return DecimalFormat("#,###").format(this)
}