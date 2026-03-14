@file:OptIn(ExperimentalCoroutinesApi::class)

package com.lihan.smartstep.stepcount.presentation

import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.placeCursorAtEnd
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lihan.smartstep.core.data.SmartStepTracker
import com.lihan.smartstep.core.domain.UserInfoDataStore
import com.lihan.smartstep.stepcount.domain.model.DailyStep
import com.lihan.smartstep.stepcount.domain.repository.SmartStepRepository
import com.lihan.smartstep.stepcount.domain.util.epochSecondToDateString
import com.lihan.smartstep.stepcount.presentation.mapper.toUi
import com.lihan.smartstep.stepcount.presentation.model.DailyStepUI
import com.lihan.smartstep.stepcount.presentation.utils.DateTimeUtils
import com.lihan.smartstep.stepcount.presentation.utils.DateTimeUtils.getDaysOfWeek
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.TextStyle
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.DurationUnit

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
                state.value.editStepsDateTextFieldState.setTextAndPlaceCursorAtEnd(
                    DateTimeUtils.getTodayDate()
                )
                state.value.editStepsStepsTextFieldState.clearText()
                _state.update { it.copy(
                    isShowEditSteps = true
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

            SmartStepAction.OnStartService -> {
                if (state.value.isTrackingStep){
                    smartStepTracker.startService()
                }
            }
            SmartStepAction.OnStopService -> {
                smartStepTracker.stopService()
            }
        }
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
                    userInfoDataStore.updateTodaySteps(steps)
                }


                val time = if (isToday){
                    state.value.time
                }else{
                    existsDailyStep?.time?:0L
                }

                smartStepTracker.updateSteps(steps)

                if (isToday){
                    userInfoDataStore.updateInitialSteps(0)
                }

                repository.updateDailyStep(
                    dailyStep = DailyStep(
                        goal = existsDailyStep?.goal?:6_000,
                        steps = steps,
                        time = time,
                        dayTimestamp = selectDate
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

    private fun onResetSteps() {
        viewModelScope.launch {
            smartStepTracker.reset()

            userInfoDataStore.updateInitialSteps(0)
            userInfoDataStore.updateTodayTimer(0)
            userInfoDataStore.updateTodaySteps(0)

            _state.update { it.copy(
                isShowResetStepsDialog = false
            ) }
        }
    }

    private fun saveStepGoal(value: String) {
        viewModelScope.launch {
            _state.update { it.copy(
                isShowStepGoal = false
            ) }
            val totalStep = value.toLongOrNull() ?: return@launch

            smartStepTracker.updateGoalSteps(totalStep)

            userInfoDataStore.updateTotalStep(totalStep)

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

}

