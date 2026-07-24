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


}