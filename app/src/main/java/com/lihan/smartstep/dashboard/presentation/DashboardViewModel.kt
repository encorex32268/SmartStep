@file:OptIn(ExperimentalCoroutinesApi::class)

package com.lihan.smartstep.dashboard.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lihan.smartstep.core.data.model.Gender
import com.lihan.smartstep.core.data.model.UserData
import com.lihan.smartstep.core.domain.AppSensorManager
import com.lihan.smartstep.core.domain.UserDataStore
import com.lihan.smartstep.core.domain.util.TimerFlow
import com.lihan.smartstep.dashboard.domain.AppPowerManager
import com.lihan.smartstep.dashboard.presentation.components.DrawerType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class DashboardViewModel(
    private val userDataStore: UserDataStore,
    private val appPowerManager: AppPowerManager,
    private val appSensorManager: AppSensorManager
) : ViewModel() {

    private var hasLoadedInitialData = false
    private var userData: UserData? = null
    private val _uiEvent = Channel<DashboardEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _state = MutableStateFlow(DashboardState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                observeLifecycle()
                observeUserStepGoal()
                observeSteps()
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
            DashboardAction.OnExitOKClick -> Unit
            DashboardAction.OnDatePickerCancelClick -> dismissDatePickerDialog()
            is DashboardAction.OnDatePickerSaveClick -> updateEditStepsDate(action.time)
            DashboardAction.OnEditStepsCancelClick -> dismissEditStepsDialog()
            DashboardAction.OnEditStepsFieldClick -> showDatePickerDialog()
            DashboardAction.OnEditStepsSaveClick -> saveEditSteps()
            DashboardAction.OnResetTodayCancelClick -> dismissResetTodayDialog()
            DashboardAction.OnResetTodayResetClick -> resetTodaySteps()
            DashboardAction.OnStartTracking -> startTracking()
            DashboardAction.OnStopTracking -> stopTracking()
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
        val currentState = state.value
        val editDate = currentState.dateTime
        val editSteps = currentState.editStepsTextFieldState.text.toString()
        //TODO: Update db

        _state.update {
            it.copy(
                isShowEditStepsDialog = false
            )
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
            .onEach { isTracking ->
                if (isTracking) {
                    appSensorManager.registerListener()
                } else {
                    appSensorManager.unregisterListener()
                }
            }.launchIn(viewModelScope)

        TimerFlow
            .timeAndEmit()
            .flatMapLatest {
                if (state.value.isTracking){
                    flow { emit(it) }
                }else emptyFlow()
            }
            .onEach { duration ->
                _state.update { it.copy(
                    time = it.time + duration
                ) }
            }
            .launchIn(viewModelScope)


        combine(
            userDataStore.todaySteps,
            appSensorManager.stepsFlow
        ) { todaySteps, perSteps ->
            todaySteps + perSteps
        }.flatMapLatest {
            if (state.value.isTracking) {
                flow { emit(it) }
            } else {
                emptyFlow()
            }
        }.onEach { currentSteps ->
            _state.update {
                it.copy(
                    steps = currentSteps
                )
            }
        }.launchIn(viewModelScope)


        _state.map { it.steps }
            .distinctUntilChanged()
            .flatMapLatest {
                if (userData != null) {
                    flow { emit(it) }
                } else emptyFlow()
            }
            .onEach { steps ->
                val height = userData?.height?.toIntOrNull()?:175
                val weight = userData?.weight?.toIntOrNull()?:65
                val genderFactor = if (userData?.gender == Gender.Male.name){
                    1.0
                }else{
                    0.9
                }
                val distance = "%.1f".format(
                    (steps * (height / 100f))/1000
                )
                val kcalPerStep = (weight * 0.005 * genderFactor).roundToInt()
                val kcal = kcalPerStep * steps

                _state.update {
                    it.copy(
                        distance = distance,
                        kcal = kcal
                    )
                }


            }.launchIn(viewModelScope)
    }


    private fun initDashboardStatus() {
        viewModelScope.launch {
            userData = userDataStore.userData.first()
            val isTracking = userDataStore.isTracking.first()
            val todaySteps = userDataStore.todaySteps.first()
            val stepGoal = userDataStore.stepGoal.first()
            _state.update {
                it.copy(
                    isTracking = isTracking,
                    steps = todaySteps,
                    stepGoal = stepGoal
                )
            }
        }


    }

}
