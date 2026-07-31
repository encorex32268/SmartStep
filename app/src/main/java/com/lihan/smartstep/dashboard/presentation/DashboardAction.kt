package com.lihan.smartstep.dashboard.presentation

import com.lihan.smartstep.dashboard.presentation.components.DrawerType

sealed interface DashboardAction {
    data object OnShowAllowAccessBottomSheet: DashboardAction
    data object OnShowEnableAccessManuallyBottomSheet: DashboardAction
    data object OnShowBackgroundAccessBottomSheet: DashboardAction
    data object OnBackgroundAccessContinueClick: DashboardAction

    data class OnDrawerItemClick(val type: DrawerType): DashboardAction
    data object OnExitOKClick: DashboardAction
    data object OnDismissExitDialog: DashboardAction
    data object OnShowStepGoalBottomSheet: DashboardAction
    data class OnStepGoalBottomSheetSaveClick(val step: String): DashboardAction
    data object OnDismissStepGoalBottomSheet: DashboardAction
    data object OnEditStepsSaveClick: DashboardAction
    data object OnEditStepsCancelClick: DashboardAction
    data object OnEditStepsFieldClick: DashboardAction
    data object OnDatePickerCancelClick: DashboardAction
    data class OnDatePickerSaveClick(val time: Long): DashboardAction
    data object OnResetTodayCancelClick: DashboardAction
    data object OnResetTodayResetClick: DashboardAction
    data object OnStartTracking: DashboardAction
    data object OnStopTracking: DashboardAction
    data object OnMoreClick: DashboardAction
}