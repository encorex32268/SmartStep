@file:OptIn(ExperimentalCoroutinesApi::class)

package com.lihan.smartstep.stepcount.presentation

import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lihan.smartstep.core.domain.AppDataStore
import com.lihan.smartstep.stepcount.data.SmartStepTracker
import com.lihan.smartstep.stepcount.domain.model.DailyStep
import com.lihan.smartstep.stepcount.domain.repository.SmartStepRepository
import com.lihan.smartstep.stepcount.domain.util.epochSecondToDateString
import com.lihan.smartstep.stepcount.presentation.mapper.toUi
import com.lihan.smartstep.stepcount.presentation.model.DailyStepUI
import com.lihan.smartstep.stepcount.presentation.utils.DateTimeUtils
import com.lihan.smartstep.stepcount.presentation.utils.DateTimeUtils.getDaysOfWeek
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.TextStyle
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.DurationUnit

class SmartStepViewModel(
    private val appDataStore: AppDataStore,
    private val repository: SmartStepRepository,
    private val smartStepTracker: SmartStepTracker
): ViewModel() {

    private var hasLoadedInitialData = false

    private val _uiEvent = Channel<SmartStepUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _state = MutableStateFlow(SmartStepState())
    val state = _state
        .onStart {
        if (!hasLoadedInitialData){
            initBackgroundAccess()
            observeData()
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
            SmartStepAction.OnStepGoalClick -> showStepGoal()
            SmartStepAction.OnDismissStepGoal -> dismissStepGoal()
            is SmartStepAction.OnStepGoalSaveClick -> saveStepGoal(action.value)
            SmartStepAction.OnExitClick -> showExitDialog()
            SmartStepAction.OnExitOkClick -> exit()
            SmartStepAction.OnDismissExitModal -> dismissExitModal()
            SmartStepAction.OnShowBackgroundAccessModalFirstTime -> showBackgroundAccessModalFirstTime()
            SmartStepAction.OnShowBackgroundAccessModal -> showBackgroundAccessModal()
            SmartStepAction.OnDismissBackgroundAccessModal -> dismissBackgroundAccessModal()
            SmartStepAction.OnShowEnableAccessModal -> showEnableAccessModal()
            SmartStepAction.OnDismissEnableAccessModal -> dismissEnableAccessModal()
            SmartStepAction.OnShowSensorsAccessModal -> showSensorsAccessModal()
            SmartStepAction.OnDismissSensorsAccessModal -> dismissSensorAccessModal()
            SmartStepAction.OnEditClick,
            SmartStepAction.OnEditStepsClick -> showEditStepsDialog()
            SmartStepAction.OnDismissEditStepsDialog -> dismissEditStepsDialog()
            SmartStepAction.OnEditStepsSaveClick -> onEditStepsSave()
            SmartStepAction.OnResumeGetGranted -> onResumeGetGranted()
            SmartStepAction.OnPermissionGranted -> permissionGranted()
            is SmartStepAction.OnUpdatePermission -> updateSensorPermission(action.isGranted)
            SmartStepAction.OnShowDatePicker -> showDatePicker()
            SmartStepAction.OnDismissDatePickerDialog -> dismissDatePickerDialog()
            is SmartStepAction.OnDatePickerSaveClick -> datePickerSaveClick(action.timestamp)
            SmartStepAction.OnResetTodayStepsClick -> showResetTodayStepsDialog()
            SmartStepAction.OnDismissResetStepsDialog -> dismissResetStepsDialog()
            SmartStepAction.OnResetStepClick -> onResetSteps()
            SmartStepAction.OnStartTracking ->resumeTracking()
            SmartStepAction.OnStopTracking -> stopTracking()
            SmartStepAction.OnStartService -> startService()
            SmartStepAction.OnStopService -> stopService()
            SmartStepAction.OnPersonSettingsClick -> Unit
            SmartStepAction.OnMenuClick -> Unit
        }
    }

    private fun initBackgroundAccess(){
        appDataStore
            .getShownBackgroundAccess()
            .onEach { isShown ->
                _state.update { it.copy(
                    isShownBackgroundAccessModal = isShown
                ) }
            }
            .launchIn(viewModelScope)
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
                    time = stepDate.countingTimestamp.milliseconds.toLong(DurationUnit.MINUTES),
                    totalStep = stepDate.goalSteps,
                    step = stepDate.steps,
                    distance = stepDate.distance,
                    calories = stepDate.calories
                ) }
            }.launchIn(viewModelScope)
    }


    private fun observeData(){
        val today = DateTimeUtils.getTodayEpochMilli()
        combine(
            state.map { it.step },
            state.map { it.totalStep },
            repository.getWeekDailySteps(today)
        ){ currentStep , currentGoal, dailySteps ->

            val hashMap = mutableMapOf<String, DailyStepUI>()

            dailySteps.sortedBy { it.dayTimestamp }.forEach { dailyStep ->
                val dailyStepUi = dailyStep.toUi()
                val short = dailyStepUi.date
                hashMap[short] = dailyStepUi
            }

            val newDailySteps = getDaysOfWeek().map {
                val short = it.getDisplayName(TextStyle.SHORT, java.util.Locale.ENGLISH)
                val todayShort = DateTimeUtils.getTodayDayOfWeekShort()
                if (short == todayShort){
                    DailyStepUI(
                        steps = currentStep.toString(),
                        date = todayShort,
                        goalSteps = currentGoal.toString(),
                        time = hashMap[short]?.time?:0L
                    )
                }else{
                    hashMap[short] ?: DailyStepUI(
                        steps = "0",
                        date = short,
                        goalSteps = "0",
                        time = 0
                    )
                }
            }
            _state.update { it.copy(
                dailySteps = newDailySteps
            ) }

        }.launchIn(viewModelScope)

    }


    private fun showExitDialog() {
        _state.update { it.copy(
            isShowExitModal = true
        ) }
    }

    private fun dismissExitModal() {
        _state.update { it.copy(
            isShowExitModal = false
        ) }
    }

    private fun exit() {
        smartStepTracker.cleanUp()
        _state.update { it.copy(
            isShowExitModal = false
        ) }
    }


    private fun showDatePicker() {
        _state.update { it.copy(
            isShowDatePicker = true
        ) }
    }
    private fun dismissDatePickerDialog() {
        _state.update { it.copy(
            isShowDatePicker = false
        ) }
    }

    private fun datePickerSaveClick(timestamp: Long) {
        state.value.editStepsDateTextFieldState.setTextAndPlaceCursorAtEnd(timestamp.epochSecondToDateString())
        _state.update { it.copy(
            isShowDatePicker = false
        ) }
    }

    private fun showResetTodayStepsDialog() {
        _state.update { it.copy(
            isShowResetStepsDialog = true
        ) }
    }

    private fun onResetSteps() {
        viewModelScope.launch {
            smartStepTracker.reset()

            appDataStore.updateInitialSteps(0)
            appDataStore.updateTodayTimer(0)
            appDataStore.updateTodaySteps(0)

            _state.update { it.copy(
                isShowResetStepsDialog = false
            ) }
        }
    }


    private fun dismissResetStepsDialog() {
        _state.update { it.copy(
            isShowResetStepsDialog = false
        ) }
    }

    private fun showEditStepsDialog() {
        state.value.editStepsDateTextFieldState.setTextAndPlaceCursorAtEnd(
            DateTimeUtils.getTodayDate()
        )
        state.value.editStepsStepsTextFieldState.clearText()
        _state.update { it.copy(
            isShowEditSteps = true
        ) }
    }

    private fun dismissEditStepsDialog() {
        _state.update { it.copy(
            isShowEditSteps = false
        ) }
    }

    private fun onEditStepsSave() {
        viewModelScope.launch {
            val stepsSaveDate = state.value.editStepsDateTextFieldState.text.toString()
            val steps = state.value.editStepsStepsTextFieldState.text.toString().toLongOrNull()?:0L
            val splitDate = stepsSaveDate.split("/")
            try {
                val year = splitDate[0].toInt()
                val month = splitDate[1].toInt()
                val date = splitDate[2].toInt()

                val selectDate = LocalDateTime
                    .of(year,month,date,0,0)
                    .atZone(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli()

                val existsDailyStep= repository.getDailyStepByDateTimestamp(selectDate).first()

                val today = DateTimeUtils.getTodayEpochMilli()

                val isToday = selectDate == today

                if (isToday){
                    appDataStore.updateInitialSteps(0)
                    smartStepTracker.updateTodaySteps(steps)
                    appDataStore.updateTodaySteps(steps)
                }

                val time = if (isToday){
                    state.value.time
                }else{
                    existsDailyStep?.time?:0L
                }


                repository.updateDailyStep(
                    dailyStep = DailyStep(
                        goal = existsDailyStep?.goal?:6_000,
                        steps = steps,
                        time = time,
                        dayTimestamp = selectDate,
                    )
                )

                _state.update { it.copy(
                    isShowEditSteps = false
                ) }


            }catch (e: Exception){
                e.printStackTrace()
                return@launch
            }
        }


    }

    private fun onResumeGetGranted() {
        _state.update { it.copy(
            motionSensorsPermissionGranted = true,
            isShowSensorsModal = false,
            isShowEnableAccessModal = false
        ) }
    }

    private fun permissionGranted() {
        _state.update { it.copy(
            isShowSensorsModal = false,
            isShowEnableAccessModal = false
        ) }
    }


    private fun showSensorsAccessModal() {
        _state.update { it.copy(
            isShowSensorsModal = true,
            isShowEnableAccessModal = false
        ) }
    }

    private fun dismissSensorAccessModal() {
        _state.update { it.copy(
            isShowSensorsModal = false
        ) }
    }

    private fun showEnableAccessModal() {
        _state.update { it.copy(
            isShowSensorsModal = false,
            isShowEnableAccessModal = true
        ) }
    }

    private fun dismissEnableAccessModal() {
        _state.update { it.copy(
            isShowEnableAccessModal = false
        ) }
    }

    private fun updateSensorPermission(isGranted: Boolean) {
        _state.update { it.copy(
            motionSensorsPermissionGranted = isGranted
        ) }
    }


    private fun showBackgroundAccessModal() {
        viewModelScope.launch {
            _state.update { it.copy(
                isShowBackgroundAccessModal = true
            ) }
        }
    }

    private fun showBackgroundAccessModalFirstTime() {
        viewModelScope.launch {
            val isShownBackgroundAccess = appDataStore.getShownBackgroundAccess().first()
            if (isShownBackgroundAccess) {
                return@launch
            } else {
                _state.update {
                    it.copy(
                        isShowBackgroundAccessModal = true
                    )
                }
                appDataStore.updateIsShownBackgroundAccess(true)
            }
        }
    }


    private fun dismissBackgroundAccessModal() {
        _state.update { it.copy(
            isShowBackgroundAccessModal = false
        ) }
    }

    private fun stopService() {
        smartStepTracker.stopService()
    }

    private fun startService() {
        if (state.value.isTrackingStep){
            smartStepTracker.startService()
        }
    }

    private fun resumeTracking() {
        smartStepTracker.startTracking()
    }

    private fun stopTracking() {

        viewModelScope.launch {
            smartStepTracker.stopTracking()

            val stepData = smartStepTracker.stepDate.first()

            appDataStore.updateTodaySteps(stepData.steps)
            appDataStore.updateTodayTimer(stepData.countingTimestamp)
            appDataStore.updateInitialSteps(0)

            repository.updateDailyStep(
                DailyStep(
                    goal = stepData.goalSteps,
                    steps = stepData.steps,
                    time =  stepData.countingTimestamp,
                    dayTimestamp = DateTimeUtils.getTodayEpochMilli(),
                )
            )
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

    private fun saveStepGoal(value: String) {
        viewModelScope.launch {
            _state.update { it.copy(
                isShowStepGoal = false
            ) }
            val totalStep = value.toLongOrNull() ?: return@launch

            smartStepTracker.updateGoalSteps(totalStep)

            appDataStore.updateTotalStep(totalStep)

            repository.updateDailyStep(
                DailyStep(
                    goal = totalStep,
                    steps = state.value.step,
                    time = state.value.time,
                    dayTimestamp = DateTimeUtils.getTodayEpochMilli()
                )
            )
        }


    }


}

