package com.lihan.smartstep.stepcount.data

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import androidx.compose.runtime.snapshotFlow
import androidx.core.content.ContextCompat.createAttributionContext
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
        val attributionContext = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            createAttributionContext(context,"step_tracking")
        } else {
            context
        }
        attributionContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    private var initialSteps = 0L
    private var lastSentSteps = 0L



    override fun trackingStep(): Flow<Long> = callbackFlow {
        val stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        if (stepSensor == null){
            trySend(0L)
            awaitClose()
            return@callbackFlow
        }

        initialSteps = userInfoDataStore.getInitialSteps().first()


        val sensorEventListener = object : SensorEventListener {
            override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}
            override fun onSensorChanged(sensorEvent: SensorEvent?) {

                val detected = (sensorEvent?.values?.getOrNull(0) ?: 0f).roundToLong()
                if (detected <= 0L) return

                if (detected < initialSteps) {
                    initialSteps = detected
                    launch { userInfoDataStore.updateInitialSteps(detected) }
                }

                if (initialSteps == 0L) {
                    initialSteps = detected
                    launch { userInfoDataStore.updateInitialSteps(detected) }
                }

                val currentSteps = (detected - initialSteps)

                val finalResult = if (currentSteps < 0) 0L else currentSteps

                if (finalResult - lastSentSteps >= 10) {
                    lastSentSteps = finalResult
                    trySend(finalResult)
                }

                Timber.d("Sensor Raw: $detected, Initial: $initialSteps, Result: ${detected - initialSteps}")

            }
        }

        sensorManager.registerListener(
            sensorEventListener,
            stepSensor,
            SensorManager.SENSOR_DELAY_UI,
            5000
        )


        awaitClose {
            sensorManager.unregisterListener(sensorEventListener)
        }
    }
}