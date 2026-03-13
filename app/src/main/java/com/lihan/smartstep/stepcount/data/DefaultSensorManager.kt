package com.lihan.smartstep.stepcount.data

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.snapshotFlow
import com.lihan.smartstep.core.domain.UserInfoDataStore
import com.lihan.smartstep.stepcount.domain.AppSensorManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.math.roundToLong

class DefaultSensorManager(
    private val context: Context,
    private val userInfoDataStore: UserInfoDataStore
) : AppSensorManager {

    private val sensorManager: SensorManager by lazy {
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    private var initialSteps = 0L
    private var lastSentSteps = 0L

    companion object{
        private const val BATCH_SIZE = 10
    }

    override fun trackingStep(): Flow<Long> = callbackFlow {
        val stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        if (stepSensor == null){
            trySend(0L)
            awaitClose()
            return@callbackFlow
        }
        val sensorEventListener = object : SensorEventListener {
            override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}
            override fun onSensorChanged(sensorEvent: SensorEvent?) {
                launch {
                    val detected = (sensorEvent?.values?.getOrNull(0) ?: 0f).roundToLong()

                    if (initialSteps == 0L) {
                        val userInitialSteps = userInfoDataStore.getInitialSteps().first()
                        initialSteps = if (userInitialSteps == 0L) {
                            userInfoDataStore.updateInitialSteps(detected)
                            detected
                        } else {
                            userInitialSteps
                        }
                    }
                    val currentSteps = detected - initialSteps

                    if (currentSteps - lastSentSteps >= 10) {
                        lastSentSteps = currentSteps
                        trySend(currentSteps)
                    }
                    Timber.d("Sensor Raw: $detected, Initial: $initialSteps, Result: ${detected - initialSteps}")
                }

            }
        }

        sensorManager.registerListener(
            sensorEventListener,
            stepSensor,
            SensorManager.SENSOR_DELAY_NORMAL,
            10_000
        )


        awaitClose {
            sensorManager.unregisterListener(sensorEventListener)
        }
    }

    //This way is sending 1.0f per step

//    override fun trackingStep(): Flow<Long> = callbackFlow {
//        val stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
//        if (stepSensor == null){
//            trySend(0L)
//            awaitClose()
//            return@callbackFlow
//        }
//        val sensorEventListener = object : SensorEventListener {
//            override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}
//            override fun onSensorChanged(sensorEvent: SensorEvent?) {
//                val detected = sensorEvent?.values?.getOrNull(0) ?: 0f
//                if (detected == 1.0f) {
//                    stepBuffer++
//                    if (stepBuffer >= BATCH_SIZE) {
//                        trySend(stepBuffer)
//                        stepBuffer = 0
//                    }
//                }
//            }
//        }
//
//        sensorManager.registerListener(
//            sensorEventListener,
//            stepSensor,
//            SensorManager.SENSOR_DELAY_NORMAL,
//            10_000_000
//        )
//
//
//        awaitClose {
//            sensorManager.unregisterListener(sensorEventListener)
//        }
//    }
}