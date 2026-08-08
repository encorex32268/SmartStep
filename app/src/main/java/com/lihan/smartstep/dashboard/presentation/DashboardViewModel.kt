@file:OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)

package com.lihan.smartstep.dashboard.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lihan.smartstep.core.data.model.Gender
import com.lihan.smartstep.core.data.model.UserData
import com.lihan.smartstep.core.domain.AppSensorManager
import com.lihan.smartstep.core.domain.DailyStepsRepository
import com.lihan.smartstep.core.domain.NetworkMirror
import com.lihan.smartstep.core.domain.UnitCalculator
import com.lihan.smartstep.core.domain.UserDataStore
import com.lihan.smartstep.core.domain.model.DailyStep
import com.lihan.smartstep.core.domain.model.formattedString
import com.lihan.smartstep.core.domain.usecase.GetStepMetricsUseCase
import com.lihan.smartstep.core.domain.util.TimerFlow
import com.lihan.smartstep.dashboard.domain.AICoach
import com.lihan.smartstep.dashboard.domain.AICoachConfig
import com.lihan.smartstep.dashboard.domain.AppPowerManager
import com.lihan.smartstep.dashboard.presentation.components.DrawerType
import com.lihan.smartstep.dashboard.presentation.model.DailyStepUi
import com.lihan.smartstep.dashboard.presentation.model.toUi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Collections.rotate
import java.util.Locale
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.DurationUnit
import kotlin.time.Instant
import kotlin.time.toDuration

class DashboardViewModel(
    private val userDataStore: UserDataStore,
    private val appPowerManager: AppPowerManager,
    private val appSensorManager: AppSensorManager,
    private val dailyStepsRepository: DailyStepsRepository,
    private val getStepMetricsUseCase: GetStepMetricsUseCase,
    private val aiCoach: AICoach,
    private val networkMirror: NetworkMirror
) : ViewModel() {

    private var hasLoadedInitialData = false

    private var aiCoachTipJob: Job?=null

    private val _uiEvent = Channel<DashboardEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _state = MutableStateFlow(DashboardState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                observeLifecycle()
                observeUserStepGoal()
                observeSteps()
                observeNetwork()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = DashboardState()
        )

    init {
        initDashboardStatus()
        initDailyStepsStatus()
        initAICoachTip()
    }

    fun onAction(action: DashboardAction) {
        when (action) {
            DashboardAction.OnShowAllowAccessBottomSheet -> showAllowAccessBottomSheet()
            DashboardAction.OnShowEnableAccessManuallyBottomSheet -> showEnableAccessManuallyBottomSheet()
            DashboardAction.OnShowBackgroundAccessBottomSheet -> showBackgroundAccessBottomSheet()
            DashboardAction.OnBackgroundAccessContinueClick -> backgroundAccessContinue()
            is DashboardAction.OnDrawerItemClick -> drawerItemClick(action.type)
            DashboardAction.OnDismissExitDialog -> dismissExitDialog()
            DashboardAction.OnShowStepGoalBottomSheet -> showStepGoalBottomSheet()
            DashboardAction.OnDismissStepGoalBottomSheet -> dismissStepGoalBottomSheet()
            is DashboardAction.OnStepGoalBottomSheetSaveClick -> saveStepGoal(action.step)
            DashboardAction.OnDatePickerCancelClick -> dismissDatePickerDialog()
            is DashboardAction.OnDatePickerSaveClick -> updateEditStepsDate(action.time)
            DashboardAction.OnEditStepsCancelClick -> dismissEditStepsDialog()
            DashboardAction.OnEditStepsFieldClick -> showDatePickerDialog()
            DashboardAction.OnEditStepsSaveClick -> saveEditSteps()
            DashboardAction.OnResetTodayCancelClick -> dismissResetTodayDialog()
            DashboardAction.OnResetTodayResetClick -> resetTodaySteps()
            DashboardAction.OnStartTracking -> startTracking()
            DashboardAction.OnStopTracking -> stopTracking()
            DashboardAction.OnExitOKClick -> Unit
            DashboardAction.OnMoreClick -> Unit
            DashboardAction.OnTryAgainClick -> initAICoachTip()
        }
    }

    private fun startTracking() {
        viewModelScope.launch {
            userDataStore.setIsTracking(true)
            _state.update {
                it.copy(
                    isTracking = true
                )
            }
        }
    }

    private fun stopTracking() {
        viewModelScope.launch {
            userDataStore.setIsTracking(false)
            _state.update {
                it.copy(
                    isTracking = false
                )
            }
        }
    }

    private fun resetTodaySteps() {
        viewModelScope.launch {
            userDataStore.setTodaySteps(0)
            _state.update {
                it.copy(
                    isShowResetDialog = false
                )
            }
        }
    }

    private fun dismissResetTodayDialog() {
        _state.update {
            it.copy(
                isShowResetDialog = false
            )
        }
    }

    private fun saveEditSteps() {
        viewModelScope.launch {
            val currentState = state.value
            val editDate = currentState.dateTime //startOfDayTime
            val editSteps = currentState.editStepsTextFieldState.text.toString()

            dailyStepsRepository.updateStepsByDate(
                dateTime = editDate,
                steps = editSteps.toIntOrNull() ?: 0
            )

            _state.update {
                it.copy(
                    isShowEditStepsDialog = false
                )
            }
        }
    }


    private fun dismissEditStepsDialog() {
        _state.update {
            it.copy(
                isShowEditStepsDialog = false
            )
        }
    }

    private fun updateEditStepsDate(time: Long) {
        _state.update {
            it.copy(
                dateTime = time,
                isShowDatePickerDialog = false
            )
        }
    }

    private fun showDatePickerDialog() {
        _state.update {
            it.copy(
                isShowDatePickerDialog = true
            )
        }
    }

    private fun dismissDatePickerDialog() {
        _state.update {
            it.copy(
                isShowDatePickerDialog = false
            )
        }
    }

    private fun saveStepGoal(step: String) {
        viewModelScope.launch {
            userDataStore.setStepGoal(steps = step.toIntOrNull() ?: 2000)
            _state.update {
                it.copy(
                    isShowStepGoalBottomSheet = false
                )
            }
        }
    }

    private fun dismissStepGoalBottomSheet() {
        _state.update {
            it.copy(
                isShowStepGoalBottomSheet = false
            )
        }
    }

    private fun showStepGoalBottomSheet() {
        _state.update {
            it.copy(
                isShowStepGoalBottomSheet = true
            )
        }
    }

    private fun dismissExitDialog() {
        _state.update {
            it.copy(
                isShowExitDialog = false
            )
        }
    }

    private fun backgroundAccessContinue() {
        _state.update {
            it.copy(
                isShowBackgroundAccessBottomSheet = false
            )
        }
    }

    private fun showEnableAccessManuallyBottomSheet() {
        _state.update {
            it.copy(
                isShowAllowAccessBottomSheet = false,
                isShowEnableAccessManuallyBottomSheet = true
            )
        }
    }

    private fun showAllowAccessBottomSheet() {
        _state.update {
            it.copy(
                isShowAllowAccessBottomSheet = true
            )
        }
    }

    private fun showBackgroundAccessBottomSheet() {
        viewModelScope.launch {
            val isShownBackgroundAccess = userDataStore.isShownBackgroundAccess.first()
            if (isShownBackgroundAccess) return@launch
            _state.update {
                it.copy(
                    isShowBackgroundAccessBottomSheet = true
                )
            }
            userDataStore.setIsShownBackgroundAccess(true)
        }
    }

    private fun drawerItemClick(type: DrawerType) {
        when (type) {
            DrawerType.FixIssue -> {
                _state.update {
                    it.copy(
                        isShowBackgroundAccessBottomSheet = true
                    )
                }
            }

            DrawerType.StepGoal -> {
                _state.update {
                    it.copy(
                        isShowStepGoalBottomSheet = true
                    )
                }
            }

            DrawerType.Exit -> {
                _state.update {
                    it.copy(
                        isShowExitDialog = true
                    )
                }
            }

            DrawerType.PersonalSettings -> {
                viewModelScope.launch {
                    _uiEvent.send(DashboardEvent.NavigateToProfileSettings)
                }
            }

            DrawerType.EditSteps -> {
                _state.update {
                    it.copy(
                        isShowEditStepsDialog = true
                    )
                }
            }

            DrawerType.RestTodaySteps -> {
                _state.update {
                    it.copy(
                        isShowResetDialog = true
                    )
                }
            }
        }
    }

    private fun observeLifecycle() {
        appPowerManager
            .isIgnoringBatteryOptimizationsFlow
            .onEach { isIgnoringBatteryOptimizations ->

                val drawerItems = if (isIgnoringBatteryOptimizations) {
                    DrawerType.entries.filter { it != DrawerType.FixIssue }
                } else {
                    DrawerType.entries
                }

                _state.update {
                    it.copy(
                        drawerItems = drawerItems
                    )
                }

            }.launchIn(viewModelScope)
    }

    private fun observeUserStepGoal() {
        userDataStore
            .stepGoal
            .onEach { stepGoal ->
                _state.update {
                    it.copy(
                        stepGoalPickerData = it.stepGoalPickerData.copy(
                            value = stepGoal.toString()
                        )
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun observeSteps() {

        _state.map { it.isTracking }
            .distinctUntilChanged()
            .drop(1)
            .onEach { isTracking ->
                if (isTracking) {
                    appSensorManager.registerListener()
                } else {
                    val currentTime = _state.value.time
                    userDataStore.setTrackingTime(currentTime.inWholeMilliseconds)
                    appSensorManager.unregisterListener()
                }
            }.launchIn(viewModelScope)


        getStepMetricsUseCase()
            .flatMapLatest { stepMetrics ->
                if (state.value.isTracking) {
                    flowOf(stepMetrics)
                } else {
                    emptyFlow()
                }
            }
            .debounce(300.milliseconds)
            .onEach { stepMetrics ->
                _state.update {
                    it.copy(
                        time = it.time + stepMetrics.time,
                        steps = stepMetrics.steps,
                        distance = stepMetrics.distance.formattedString(),
                        kcal = stepMetrics.kcal,
                        stepGoal = stepMetrics.stepGoal
                    )
                }
            }.launchIn(viewModelScope)

    }


    private fun initDashboardStatus() {
        viewModelScope.launch {
            val isTracking = userDataStore.isTracking.first()
            val savedTimeMillis = userDataStore.trackingTime.first()
            val stepMetrics = getStepMetricsUseCase().first()
            val todaySteps = stepMetrics.steps
            val stepGoal = stepMetrics.stepGoal

            _state.update {
                it.copy(
                    isTracking = isTracking,
                    steps = todaySteps,
                    stepGoal = stepGoal,
                    distance = stepMetrics.distance.formattedString(),
                    kcal = stepMetrics.kcal,
                    time = savedTimeMillis.milliseconds,
                )
            }
        }
    }

    private fun initDailyStepsStatus(){
        combine(
            dailyStepsRepository.getWeekDailyStepsList(),
            appSensorManager.stepsFlow,
            userDataStore.stepGoal,
        ){ dailySteps , steps , stepGoal ->
            dailySteps.toDailyStepUiList(
                todaySteps = steps,
                todayStepsGoal = stepGoal
            )

        }.onEach { dailyStepUis ->
            _state.update {
                it.copy(
                    dailySteps = dailyStepUis
                )
            }
        }.launchIn(viewModelScope)
    }


    private fun List<DailyStep>.toDailyStepUiList(
        todaySteps: Int,
        todayStepsGoal: Int
    ): List<DailyStepUi> {
        val today = DayOfWeek.from(LocalDateTime.now()).value
        val dayOfWeeksStartFromSun = DayOfWeek.entries.toMutableList().apply { rotate(this, 1) }
        return dayOfWeeksStartFromSun.map { dayOfWeek ->
            val dayName = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.US)
            if (dayOfWeek.value == today) {
                DailyStepUi(
                    day = dayName,
                    steps = todaySteps,
                    stepsGoal = todayStepsGoal
                )
            } else {
                val dailyStep = this.find { dailyStep ->
                    val stepDayOfWeek = java.time.Instant.ofEpochMilli(dailyStep.createAt)
                        .atZone(ZoneId.systemDefault())
                        .dayOfWeek
                    stepDayOfWeek.value == dayOfWeek.value
                }
                if (dailyStep == null) {
                    DailyStepUi(
                        day = dayName,
                        steps = 0,
                        stepsGoal = 0
                    )
                } else {
                    DailyStepUi(
                        day = dayName,
                        steps = dailyStep.steps,
                        stepsGoal = dailyStep.stepsGoal
                    )
                }
            }
        }
    }

    private fun initAICoachTip(){
        //Avoid more times click at the same time.
        aiCoachTipJob?.cancel()
        aiCoachTipJob = viewModelScope.launch {
            delay(300.milliseconds)
            val currentState = state.value
            val result = aiCoach
                .generateCoachResponseWithStepData(
                    userMessage = "",
                    currentSteps = currentState.steps,
                    stepGoal = currentState.stepGoal,
                    spentTimeMinutes = currentState.time.inWholeMinutes.toInt(),
                    distanceKm = currentState.distance.toDoubleOrNull()?:0.0,
                    caloriesBurned = currentState.kcal,
                    otherRule = AICoachConfig.TIP_PROMPT
                )
            _state.update { it.copy(
                aiCoachTip = result
            ) }
        }
    }

    private fun observeNetwork(){
        networkMirror
            .isConnecting
            .onEach {  isConnecting ->
                _state.update { it.copy(
                    isNetworkError = !isConnecting
                ) }
            }.launchIn(viewModelScope)
    }
}
