package com.lihan.smartstep.dashboard.domain

import kotlinx.coroutines.flow.Flow

interface AppPowerManager {
    val isIgnoringBatteryOptimizationsFlow: Flow<Boolean>
}