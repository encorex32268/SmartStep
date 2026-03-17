package com.lihan.smartstep.stepcount.domain.sensor

import kotlinx.coroutines.flow.Flow

interface StepSensorManager {
    fun trackingStep(): Flow<Long>
}