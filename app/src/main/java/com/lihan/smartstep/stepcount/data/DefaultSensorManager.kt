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
    private val context: Context
) : AppSensorManager {

    private val sensorManager: SensorManager by lazy {
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    private var stepBuffer = 0L

    companion object{
        private const val BATCH_SIZE = 10
    }
    override fun trackingStep(): Flow<Long> = callbackFlow {
        val stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
        val sensorEventListener = object : SensorEventListener {
            override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}
            override fun onSensorChanged(sensorEvent: SensorEvent?) {
                val detected = sensorEvent?.values?.getOrNull(0) ?: 0f
                if (detected == 1.0f) {
                    stepBuffer++
                    if (stepBuffer >= BATCH_SIZE) {
                        trySend(stepBuffer)
                        stepBuffer = 0
                    }
                }
            }
        }

        sensorManager.registerListener(
            sensorEventListener,
            stepSensor,
            SensorManager.SENSOR_DELAY_NORMAL,
            10_000_000
        )


        awaitClose {
            sensorManager.unregisterListener(sensorEventListener)
        }
    }
}