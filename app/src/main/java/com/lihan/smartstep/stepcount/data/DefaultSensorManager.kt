package com.lihan.smartstep.stepcount.data

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.lihan.smartstep.core.domain.UserInfoDataStore
import com.lihan.smartstep.stepcount.domain.AppSensorManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DefaultSensorManager(
    private val context: Context,
    private val userInfoDataStore: UserInfoDataStore
) : AppSensorManager {
    private val sensorManager: SensorManager by lazy {
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    override fun trackingStep(): Flow<Long> = callbackFlow {
        val stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        val sensorEventListener = object : SensorEventListener {
            override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}
            override fun onSensorChanged(sensorEvent: SensorEvent?) {
                this@callbackFlow.launch {
                    val totalStepsFloat = sensorEvent?.values?.firstOrNull()
                    if (totalStepsFloat == null) {
                        trySend(0)
                        return@launch
                    }
                    val totalSteps = totalStepsFloat.toLong()
                    val deviceInitSteps = userInfoDataStore.getDeviceInitSteps().first()

                    if (deviceInitSteps == 0L) {
                        userInfoDataStore.updateDeviceInitSteps(totalSteps)
                    }
                    val trackingStep = if (deviceInitSteps == 0L){
                        0
                    }else{
                        totalSteps - deviceInitSteps
                    }
                    //a batch of 10 or more to update state
                    if (trackingStep % 10 >= 0){
                        trySend(trackingStep)
                    }
                }
            }
        }

        sensorManager.registerListener(
            sensorEventListener,
            stepSensor,
            SensorManager.SENSOR_DELAY_UI
        )


        awaitClose {
            sensorManager.unregisterListener(sensorEventListener)
        }
    }
}