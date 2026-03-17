@file:OptIn(ExperimentalCoroutinesApi::class)

package com.lihan.smartstep.core.data

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Build
import androidx.core.content.ContextCompat.createAttributionContext
import androidx.lifecycle.viewModelScope
import com.lihan.smartstep.core.data.service.CountingStepService
import com.lihan.smartstep.core.domain.UserInfoDataStore
import com.lihan.smartstep.onboarding.presentation.model.Gender
import com.lihan.smartstep.stepcount.domain.AppSensorManager
import com.lihan.smartstep.stepcount.domain.Timer
import com.lihan.smartstep.stepcount.domain.model.DailyStep
import com.lihan.smartstep.stepcount.domain.repository.SmartStepRepository
import com.lihan.smartstep.stepcount.presentation.utils.DateTimeUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import timber.log.Timber
import kotlin.math.roundToInt
import kotlin.math.roundToLong

class SmartStepTracker(
    val applicationScope: CoroutineScope,
    private val applicationContext: Context,
    private val appSensorManager: AppSensorManager,
    private val userInfoDataStore: UserInfoDataStore
) {

    companion object{
        private const val DEFAULT_GOAL_STEPS = 6_000L
    }

    private val _stepData = MutableStateFlow(StepData())
    val stepDate = _stepData.asStateFlow()

    private val _isTracking = MutableStateFlow(false)
    val isTracking = _isTracking.asStateFlow()

    init {

        applicationScope.launch {
           initialStepData()
        }

        isTracking
            .flatMapLatest { isTracking ->
                if (isTracking){
                    appSensorManager.trackingStep()
                }else emptyFlow()
            }.distinctUntilChanged()
            .onEach { step ->
                calculateDataByStep(step)
            }
            .launchIn(applicationScope)

        isTracking
            .flatMapLatest { isTracking ->
                if (isTracking){
                    Timer.emitDuration()
                }else {
                    emptyFlow()
                }
            }.onEach { duration ->
               _stepData.update { it.copy(
                   countingTimestamp = it.countingTimestamp + duration.inWholeMilliseconds
               ) }
            }.launchIn(applicationScope)

    }


    private suspend fun initialStepData(){

        val todaySteps = userInfoDataStore.getTodaySteps().first()

        val distance = getDistance(todaySteps)
        val calories = getCalories(todaySteps)

        val timer = userInfoDataStore.getTodayTimer().first()
        val goalSteps = userInfoDataStore.getTotalStep().first()

        _stepData.update { it.copy(
            goalSteps = goalSteps,
            steps = todaySteps,
            countingTimestamp = timer,
            calories = calories,
            distance = distance
        ) }
    }

    private suspend fun calculateDataByStep(step: Long) {

        val newSteps = userInfoDataStore.getTodaySteps().first() + step

        val distance = getDistance(newSteps)
        val calories = getCalories(newSteps)

        _stepData.update { it.copy(
            steps = newSteps,
            calories = calories,
            distance = distance
        ) }
    }

    private suspend fun getCalories(steps: Long): Long {
        val weight = userInfoDataStore.getWeight().first()
        val isMale = userInfoDataStore.getGender().first() == Gender.MALE
        val genderRate = if (isMale) 1f else 0.9f

        val calories = weight * genderRate * 0.0005 * steps

        return calories.roundToLong()
    }

    private suspend fun getDistance(steps: Long): Double {
        val height = userInfoDataStore.getHeight().first()
        val distance = ((height * 0.415) * steps) / 100 / 1000
        return (distance * 10).roundToInt() / 10.0
    }


    fun startTracking() {
        _isTracking.update { true }
    }

    fun stopTracking(){
        _isTracking.update { false }
    }


    fun updateGoalSteps(goalSteps: Long) {
        _stepData.update { it.copy(
            goalSteps = goalSteps
        ) }
    }
    fun updateTodaySteps(todaySteps: Long) {
        _stepData.update { it.copy(
            steps = todaySteps
        ) }
    }

    fun startService(){
        val canRunInBackground = applicationContext.isIgnoringBatteryOptimizations()
        val hasNotificationPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            applicationContext.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
        val hasActivityRecognitionPermission =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                applicationContext.checkSelfPermission(Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_GRANTED
            } else {
                applicationContext.checkSelfPermission("com.google.android.gms.permission.ACTIVITY_RECOGNITION")  == PackageManager.PERMISSION_GRANTED
            }

        val canStartService = canRunInBackground && hasNotificationPermission && hasActivityRecognitionPermission

        if (!canStartService) {
            stopService()
            return
        }

        val intent = Intent(
            applicationContext, CountingStepService::class.java
        ).apply {
            action = CountingStepService.START
        }

        applicationContext.startService(intent)

    }

    fun stopService(){
        val intent = Intent(
            applicationContext, CountingStepService::class.java
        ).apply {
            action = CountingStepService.STOP
        }

        applicationContext.startService(intent)
    }

    fun reset(){
        _stepData.update {
            StepData(goalSteps = DEFAULT_GOAL_STEPS)
        }
    }

}