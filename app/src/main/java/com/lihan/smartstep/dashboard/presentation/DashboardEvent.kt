package com.lihan.smartstep.dashboard.presentation

sealed interface DashboardEvent {
    data object NavigateToProfileSettings: DashboardEvent
}