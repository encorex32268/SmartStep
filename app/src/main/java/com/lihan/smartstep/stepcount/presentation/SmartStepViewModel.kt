@file:OptIn(ExperimentalCoroutinesApi::class)

package com.lihan.smartstep.stepcount.presentation

import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.text.intl.Locale
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lihan.smartstep.core.domain.UserInfoDataStore
import com.lihan.smartstep.onboarding.presentation.model.Gender
import com.lihan.smartstep.stepcount.domain.AppSensorManager
import com.lihan.smartstep.stepcount.domain.Timer
import com.lihan.smartstep.stepcount.domain.model.DailyStep
import com.lihan.smartstep.stepcount.domain.repository.SmartStepRepository
import com.lihan.smartstep.stepcount.presentation.components.DailyStep
import com.lihan.smartstep.stepcount.presentation.mapper.toUi
import com.lihan.smartstep.stepcount.presentation.model.DailyStepUI
import com.lihan.smartstep.stepcount.presentation.utils.DateTimeUtils
import com.lihan.smartstep.stepcount.presentation.utils.DateTimeUtils.getCustomDayOfWeek
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.TextStyle
import kotlin.math.roundToInt
import kotlin.math.roundToLong
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class SmartStepViewModel(
    private val userInfoDataStore: UserInfoDataStore,
    private val appSensorManager: AppSensorManager,
    private val repository: SmartStepRepository
): ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(SmartStepState())
    val state = _state
        .onStart {
        if (!hasLoadedInitialData){
            observeTotalStep()
            initBackgroundAccess()
            initSensorManager()
            getWeekDailySteps()
            hasLoadedInitialData = true
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        SmartStepState()
    )

    private fun getWeekDailySteps() {
        val today = DateTimeUtils.getTodayEpochMilli()
        repository
            .getWeekDailySteps(today)
            .onEach { dailySteps ->
                val hashMap = mutableMapOf<String, DailyStepUI>()
                dailySteps.sortedBy { it.timestamp }.forEach { dailyStep ->
                    val dailyStepUi = dailyStep.toUi()
                    val short = dailyStepUi.timestamp.epochMilToDayOfWeekShort()
                    hashMap[short] = dailyStepUi
                }

                val newDailySteps = getCustomDayOfWeek().map {
                    val short = it.getDisplayName(TextStyle.SHORT, java.util.Locale.ENGLISH)
                    hashMap[short] ?: DailyStepUI(
                        steps = "0",
                        date = short,
                        goalSteps = "0",
                        timestamp = 0
                    )
                }
                _state.update { it.copy(
                    dailySteps = newDailySteps
                ) }
            }
            .launchIn(viewModelScope)
    }


    fun onAction(action: SmartStepAction){
        when(action){
            SmartStepAction.OnDismissStepGoal -> dismissStepGoal()
            SmartStepAction.OnStepGoalClick -> showStepGoal()
            SmartStepAction.OnExitOkClick -> {
                _state.update { it.copy(
                    isShowExitModal = false
                ) }
            }
            SmartStepAction.OnMenuClick -> Unit
            SmartStepAction.OnPersonSettingsClick -> Unit
            SmartStepAction.OnShowBackgroundAccessModal -> {
                viewModelScope.launch {
                    _state.update { it.copy(
                        isShowBackgroundAccessModal = true
                    ) }
                }
            }
            SmartStepAction.OnShowBackgroundAccessModalFirstTime ->{
                viewModelScope.launch {
                    val isShownBackgroundAccess = userInfoDataStore.getShownBackgroundAccess().first()
                    if (isShownBackgroundAccess) {
                        return@launch
                    } else {
                        _state.update {
                            it.copy(
                                isShowBackgroundAccessModal = true
                            )
                        }
                        userInfoDataStore.updateIsShownBackgroundAccess(true)
                    }
                }
            }
            SmartStepAction.OnShowEnableAccessModal -> {
                _state.update { it.copy(
                    isShowSensorsModal = false,
                    isShowEnableAccessModal = true
                ) }
            }
            SmartStepAction.OnShowSensorsAccessModal -> {
                _state.update { it.copy(
                    isShowSensorsModal = true,
                    isShowEnableAccessModal = false
                ) }
            }
            is SmartStepAction.OnUpdatePermission -> {
                _state.update { it.copy(
                    motionSensorsPermissionGranted = action.isGranted,

                ) }
            }

            SmartStepAction.OnDismissEnableAccessModal -> {
                _state.update { it.copy(
                    isShowEnableAccessModal = false
                ) }
            }
            SmartStepAction.OnDismissSensorsAccessModal ->{
                _state.update { it.copy(
                    isShowSensorsModal = false
                ) }
            }

            SmartStepAction.OnResumeGetGranted -> {
                _state.update { it.copy(
                    motionSensorsPermissionGranted = true,
                    isShowSensorsModal = false,
                    isShowEnableAccessModal = false
                ) }
            }

            SmartStepAction.OnDismissBackgroundAccessModal -> {
                _state.update { it.copy(
                    isShowBackgroundAccessModal = false
                ) }
            }

            is SmartStepAction.OnStepGoalSaveClick -> saveStepGoal(action.value)
            SmartStepAction.OnDismissExitModal -> {
                _state.update { it.copy(
                    isShowExitModal = false
                ) }
            }
            SmartStepAction.OnExitClick -> {
                _state.update { it.copy(
                    isShowExitModal = true
                ) }
            }

            SmartStepAction.OnPermissionGranted -> {
                _state.update { it.copy(
                    isShowSensorsModal = false,
                    isShowEnableAccessModal = false
                ) }
            }
            SmartStepAction.OnEditClick,
            SmartStepAction.OnEditStepsClick -> {
                state.value.editStepsDateTextFieldState.clearText()
                _state.update { it.copy(
                    isShowEditSteps = true,
                    step = 0L
                ) }
            }
            SmartStepAction.OnResetTodayStepsClick ->{
                _state.update { it.copy(
                    isShowResetStepsDialog = true
                ) }
            }

            SmartStepAction.OnDismissDatePickerDialog -> {
                _state.update { it.copy(
                    isShowDatePicker = false
                ) }
            }
            SmartStepAction.OnDismissEditStepsDialog -> {
                _state.update { it.copy(
                    isShowEditSteps = false
                ) }
            }
            SmartStepAction.OnDismissResetStepsDialog -> {
                _state.update { it.copy(
                    isShowResetStepsDialog = false
                ) }
            }
            SmartStepAction.OnEditStepsSaveClick -> onEditStepsSave()
            SmartStepAction.OnResetStepClick -> onResetSteps()
            SmartStepAction.OnShowDatePicker -> {
                _state.update { it.copy(
                    isShowDatePicker = true
                ) }
            }

            is SmartStepAction.OnDatePickerSaveClick -> {
                state.value.editStepsDateTextFieldState.setTextAndPlaceCursorAtEnd(action.timestamp.epochSecondToDateString())
                _state.update { it.copy(
                    isShowDatePicker = false
                ) }
            }
            SmartStepAction.OnDismissDatePicker -> {
                state.value.editStepsDateTextFieldState.clearText()
                _state.update { it.copy(
                    isShowDatePicker = false
                ) }
            }

            SmartStepAction.OnResumeCounting ->{
                _state.update { it.copy(
                    isTrackingStep = true
                ) }
            }
            SmartStepAction.OnStopCounting -> {
                _state.update { it.copy(
                    isTrackingStep = false
                ) }
            }
        }
    }

    private fun onEditStepsSave() {

    }

    private fun onResetSteps() {
        viewModelScope.launch {
            userInfoDataStore.updateTodaySteps(0)
            userInfoDataStore.updateTodayTimer(0L)
        }
    }

    private fun initSensorManager() {
        viewModelScope.launch {
            val userTodaySteps = userInfoDataStore.getTodaySteps().first()
            val timer = userInfoDataStore.getTodayTimer().first().toDuration(DurationUnit.MILLISECONDS)
            _state.update {it.copy(
                step = userTodaySteps,
                timer = timer
            ) }
            calculateDataByStep(0)

        state.map { it.isTrackingStep }
            .distinctUntilChanged()
            .flatMapLatest { isTracking ->
                if (isTracking){
                    appSensorManager.trackingStep()
                }else{
                    userInfoDataStore.updateTodayTimer(state.value.timer.inWholeMilliseconds)
                    userInfoDataStore.updateTodaySteps(state.value.step)
                    saveDataToDB()
                    emptyFlow()
                }
            }.onEach { step ->
                calculateDataByStep(step)
                saveDataToDB()
            }
            .launchIn(this)

        state.map { it.isTrackingStep }
            .distinctUntilChanged()
            .flatMapLatest { isTracking ->
                if (isTracking){
                    Timer.emitDuration()
                }else {
                    emptyFlow()
                }
            }.onEach { duration ->
                _state.update { it.copy(
                    timer = it.timer + duration
                ) }
            }.launchIn(this)

        }


    }

    private suspend fun calculateDataByStep(step: Long) {

        val newStep = state.value.step + step

        val height = userInfoDataStore.getHeight().first()
        val distance = ((height * 0.415) * newStep)/100/1000

        val weight = userInfoDataStore.getWeight().first()
        val isMale = userInfoDataStore.getGender().first() == Gender.MALE
        val genderRate = if (isMale) 1f else 0.9f
        val calories = weight * genderRate * 0.0005 * newStep

        _state.update { it.copy(
            step = newStep,
            calories = calories.roundToLong(),
            distance = (distance * 10).roundToInt() / 10.0
        ) }
    }


    private fun saveStepGoal(value: String) {
        _state.update { it.copy(
            isShowStepGoal = false
        ) }
        val totalStep = value.toLongOrNull() ?: return

        viewModelScope.launch {
            userInfoDataStore.updateTotalStep(totalStep)
        }

    }

    private fun showStepGoal() {
        _state.update { it.copy(
            isShowStepGoal = true
        ) }
    }

    private fun dismissStepGoal() {
        _state.update { it.copy(
            isShowStepGoal = false
        ) }
    }

    private fun observeTotalStep(){
        userInfoDataStore
            .getTotalStep()
            .onEach { totalStep ->
                _state.update { it.copy(
                    totalStep = totalStep
                ) }
            }
            .launchIn(viewModelScope)
    }

    private fun initBackgroundAccess(){
        userInfoDataStore
            .getShownBackgroundAccess()
            .onEach { isShown ->
                _state.update { it.copy(
                    isShownBackgroundAccessModal = isShown
                ) }
            }
            .launchIn(viewModelScope)
    }


    private fun saveDataToDB(){
        viewModelScope.launch {
            val dailyStep = DailyStep(
                steps = state.value.step,
                goal = state.value.totalStep,
                timestamp = DateTimeUtils.getTodayEpochMilli()
            )
            repository.updateDailyStep(dailyStep)
        }
    }
}


fun Long.epochSecondToDateString(): String {
    val zoneId = ZoneId.systemDefault()
    val offset = zoneId.rules.getOffset(Instant.ofEpochSecond(this))

    val localDateTime = LocalDateTime.ofEpochSecond(
        this, 0, offset
    )
    val year = localDateTime.year + 1920
    val month = localDateTime.monthValue
    val day = localDateTime.dayOfMonth
    return "$year/$month/$day"
}

fun Long.epochMilToDayOfWeekShort(): String {
    val zoneId = ZoneId.systemDefault()
    val localDateTime = LocalDateTime.ofInstant(
        Instant.ofEpochMilli(this),
        zoneId
    )
    return localDateTime.dayOfWeek.getDisplayName(TextStyle.SHORT, java.util.Locale.ENGLISH)
}