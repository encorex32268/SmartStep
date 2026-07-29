package com.lihan.smartstep.core.data

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.lihan.smartstep.core.domain.AppSensorManager
import com.lihan.smartstep.core.domain.UserDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


class StepsSensorManager(
    private val context: Context,
    private val coroutineScope: CoroutineScope,
    private val userDataStore: UserDataStore
) : AppSensorManager, SensorEventListener {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
    private val stepSensor: Sensor? = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

    private var isDataStoreLoaded = false

    private var sessionStartSensorValue: Float? = null

    private var baseAccumulatedSteps: Int = 0

    private val _currentSteps = MutableStateFlow(0)
    override val stepsFlow = _currentSteps.asStateFlow()

    init {
        userDataStore
            .todaySteps
            .onEach { todaySteps ->
                baseAccumulatedSteps = todaySteps
            }
            .launchIn(coroutineScope)

        isDataStoreLoaded = true
    }

    override fun registerListener() {
        if (!context.hasActivityRecognitionPermission) return
        println("SensorManager: registerListener")
        sessionStartSensorValue = null
        _currentSteps.update { 0 }

        stepSensor?.let { sensor ->
            sensorManager?.registerListener(
                this@StepsSensorManager,
                sensor,
                SensorManager.SENSOR_DELAY_UI
            )
        }
    }

    override fun unregisterListener() {
        println("SensorManager: unregisterListener")
        sensorManager?.unregisterListener(this@StepsSensorManager)

        val sessionSteps = _currentSteps.value
        if (sessionSteps > 0) {
            coroutineScope.launch {
                userDataStore.setTodaySteps(baseAccumulatedSteps + sessionSteps)
                _currentSteps.update { 0 }
            }
        }
        sessionStartSensorValue = null

    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        //Nothing to do.
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type != Sensor.TYPE_STEP_COUNTER) return
        if (!isDataStoreLoaded) return

        val totalStepSinceBoot = event.values?.firstOrNull() ?: return
        println("onSensorChanged: totalStepSinceBoot = $totalStepSinceBoot")

        val startValue = sessionStartSensorValue ?: totalStepSinceBoot.also {
            sessionStartSensorValue = it
        }

        // mean phone reboot.
        if (totalStepSinceBoot < startValue) {
            sessionStartSensorValue = totalStepSinceBoot
            _currentSteps.update { 0 }
            return
        }

        val stepsInThisSession = (totalStepSinceBoot - startValue).roundToInt()

        _currentSteps.update { stepsInThisSession }
    }
}