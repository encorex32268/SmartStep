@file:OptIn(ExperimentalCoroutinesApi::class)

package com.lihan.smartstep.stepcount.presentation

import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lihan.smartstep.core.data.SmartStepTracker
import com.lihan.smartstep.core.domain.UserInfoDataStore
import com.lihan.smartstep.stepcount.domain.model.DailyStep
import com.lihan.smartstep.stepcount.domain.repository.SmartStepRepository
import com.lihan.smartstep.stepcount.domain.util.epochMilToDayOfWeekShort
import com.lihan.smartstep.stepcount.domain.util.epochSecondToDateString
import com.lihan.smartstep.stepcount.presentation.mapper.toUi
import com.lihan.smartstep.stepcount.presentation.model.DailyStepUI
import com.lihan.smartstep.stepcount.presentation.utils.DateTimeUtils
import com.lihan.smartstep.stepcount.presentation.utils.DateTimeUtils.getCustomDayOfWeek
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.format.TextStyle
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class SmartStepViewModel(
    private val userInfoDataStore: UserInfoDataStore,
    private val repository: SmartStepRepository,
    private val smartStepTracker: SmartStepTracker
): ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(SmartStepState())
    val state = _state
        .onStart {
        if (!hasLoadedInitialData){
            initBackgroundAccess()
            getWeekDailySteps()
            observeSmartStepTracker()
            observeSmartStepTrackerStepDate()
            hasLoadedInitialData = true
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        SmartStepState()
    )

    fun onAction(action: SmartStepAction){
        when(action){
            SmartStepAction.OnDismissStepGoal -> dismissStepGoal()
            SmartStepAction.OnStepGoalClick -> showStepGoal()
            SmartStepAction.OnExitOkClick -> {
                smartStepTracker.stopTracking()
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
                smartStepTracker.startTracking()
            }
            SmartStepAction.OnStopCounting -> {
                smartStepTracker.stopTracking()
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

    private fun observeSmartStepTracker() {
        smartStepTracker.isTracking
            .onEach { isTracking ->
                _state.update { it.copy(
                    isTrackingStep = isTracking
                ) }
            }
            .launchIn(viewModelScope)
    }


    private fun observeSmartStepTrackerStepDate() {
        smartStepTracker.stepDate
            .onEach { stepDate ->
                _state.update { it.copy(
                    timer = stepDate.countingTimestamp.milliseconds.toInt(DurationUnit.MINUTES),
                    totalStep = stepDate.goalSteps,
                    step = stepDate.steps,
                    distance = stepDate.distance,
                    calories = stepDate.calories
                ) }
            }.launchIn(viewModelScope)
    }

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
}

