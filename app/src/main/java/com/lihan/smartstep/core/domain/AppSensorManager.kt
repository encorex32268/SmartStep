package com.lihan.smartstep.core.domain

import kotlinx.coroutines.flow.StateFlow

interface AppSensorManager {
   val stepsFlow: StateFlow<Int>
   fun registerListener()
   fun unregisterListener()
}