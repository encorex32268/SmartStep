package com.lihan.smartstep.stepcount.domain

import kotlinx.coroutines.flow.Flow

interface AppSensorManager {
    fun trackingStep(): Flow<Long>
}