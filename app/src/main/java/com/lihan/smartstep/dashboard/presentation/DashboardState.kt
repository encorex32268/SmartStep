package com.lihan.smartstep.dashboard.presentation

import androidx.compose.foundation.text.input.TextFieldState
import com.lihan.smartstep.core.presentation.components.WheelPickerData
import com.lihan.smartstep.dashboard.presentation.components.DrawerType
import com.lihan.smartstep.dashboard.presentation.model.DailyStepUi
import kotlin.time.Clock
import kotlin.time.Duration

data class DashboardState(
    val drawerItems: List<DrawerType> = emptyList(),
    val isShowAllowAccessBottomSheet: Boolean = false,
    val isShowEnableAccessManuallyBottomSheet: Boolean = false,
    val isShowBackgroundAccessBottomSheet: Boolean = false,
    val isShowStepGoalBottomSheet: Boolean = false,
    val isShowExitDialog: Boolean = false,
    val stepGoal: Int = 2000,
    val steps: Int = 0,
    val isTracking: Boolean = false,
    val stepGoalPickerData: WheelPickerData = WheelPickerData(
        value = stepGoal.toString(),
        items = (1000..40000 step 1000).sortedDescending().map { it.toString() }
    ),
    val isShowResetDialog: Boolean = false,
    val isShowEditStepsDialog: Boolean = false,
    val dateTime: Long = Clock.System.now().toEpochMilliseconds(),
    val editStepsTextFieldState: TextFieldState = TextFieldState("0"),
    val isShowDatePickerDialog: Boolean = false,
    val distance: String = "0.0",
    val kcal: Int = 0,
    val time: Duration = Duration.ZERO,
    val dailySteps: List<DailyStepUi> = emptyList()
){
    val timeString: String
        get() = time.toComponents { minutes, _, _ ->
            "%d".format(minutes)
        }


}
