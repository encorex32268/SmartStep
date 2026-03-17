package com.lihan.smartstep.stepcount.data.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.lihan.smartstep.core.domain.AppDataStore
import com.lihan.smartstep.stepcount.domain.sensor.StepSensorManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.math.roundToLong

class DefaultSensorManager(
    private val context: Context,
    private val appDataStore: AppDataStore
) : StepSensorManager {

    private val sensorManager: SensorManager by lazy {
        val attributionContext = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ContextCompat.createAttributionContext(context, "step_tracking")
        } else {
            context
        }
        attributionContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    private var initialSteps = 0L
    private var lastSentSteps = 0L

    override fun trackingStep(): Flow<Long> = callbackFlow {
        val stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        if (stepSensor == null) {
            trySend(0L)
            awaitClose()
            return@callbackFlow
        }

        initialSteps = appDataStore.getInitialSteps().first()

        val sensorEventListener = object : SensorEventListener {
            override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}
            override fun onSensorChanged(sensorEvent: SensorEvent?) {
                launch {
                    val detected = (sensorEvent?.values?.getOrNull(0) ?: 0f).roundToLong()
                    if (detected <= 0L) return@launch

                    if (detected < initialSteps) {
                        initialSteps = detected
                        appDataStore.updateInitialSteps(detected)
                    }

                    if (initialSteps == 0L) {
                        lastSentSteps = 0L
                        initialSteps = detected
                        launch { appDataStore.updateInitialSteps(detected) }
                    }

                    val currentSteps = (detected - initialSteps)

                    val finalResult = if (currentSteps < 0) 0L else currentSteps


                    if (finalResult - lastSentSteps >= 10) {
                        if (lastSentSteps != 0L) {
                            trySend(finalResult)
                        }
                        lastSentSteps = finalResult
                    }

                    Timber.Forest.d("finalResult: $finalResult , lastSentSteps: $lastSentSteps")
                    Timber.Forest.d("Sensor Raw: $detected, Initial: $initialSteps, Result: ${detected - initialSteps}")

                }
            }
        }

        sensorManager.registerListener(
            sensorEventListener,
            stepSensor,
            SensorManager.SENSOR_DELAY_UI,
            5000
        )


        awaitClose {
            Timber.Forest.d("Closed Default Sensor Manager")
            sensorManager.unregisterListener(sensorEventListener)

        }
    }
}