package com.lihan.smartstep.dashboard.presentation

import androidx.compose.foundation.text.input.TextFieldState
import com.lihan.smartstep.core.presentation.components.WheelPickerData
import com.lihan.smartstep.dashboard.presentation.components.DrawerType
import kotlin.time.Clock

data class DashboardState(
    val drawerItems: List<DrawerType> = emptyList(),
    val isShowAllowAccessBottomSheet: Boolean = false,
    val isShowEnableAccessManuallyBottomSheet: Boolean = false,
    val isShowBackgroundAccessBottomSheet: Boolean = false,
    val isShowStepGoalBottomSheet: Boolean = false,
    val isShowExitDialog: Boolean = false,
    val stepGoal: String = "2000",
    val stepGoalPickerData: WheelPickerData = WheelPickerData(
        value = stepGoal,
        items = (1000..40000 step 1000).sortedDescending().map { it.toString() }
    ),
    val isShowResetDialog: Boolean = false,
    val isShowEditStepsDialog: Boolean = false,
    val dateTime: Long = Clock.System.now().toEpochMilliseconds(),
    val editStepsTextFieldState: TextFieldState = TextFieldState("0"),
    val isShowDatePickerDialog: Boolean = false,
)
