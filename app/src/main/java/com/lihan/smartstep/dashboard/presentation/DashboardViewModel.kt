package com.lihan.smartstep.dashboard.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lihan.smartstep.core.domain.UserDataStore
import com.lihan.smartstep.dashboard.domain.AppPowerManager
import com.lihan.smartstep.dashboard.presentation.components.DrawerType
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val userDataStore: UserDataStore,
    private val appPowerManager: AppPowerManager
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _uiEvent = Channel<DashboardEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _state = MutableStateFlow(DashboardState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                observeLifecycle()
                observeUserStepGoal()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = DashboardState()
        )

    fun onAction(action: DashboardAction) {
        when (action) {
            DashboardAction.OnShowAllowAccessBottomSheet -> {
                _state.update {
                    it.copy(
                        isShowAllowAccessBottomSheet = true
                    )
                }
            }

            DashboardAction.OnShowEnableAccessManuallyBottomSheet -> {
                _state.update {
                    it.copy(
                        isShowAllowAccessBottomSheet = false,
                        isShowEnableAccessManuallyBottomSheet = true
                    )
                }
            }

            DashboardAction.OnShowBackgroundAccessBottomSheet -> {
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

            DashboardAction.OnBackgroundAccessContinueClick -> {
                _state.update {
                    it.copy(
                        isShowBackgroundAccessBottomSheet = false
                    )
                }
            }

            is DashboardAction.OnDrawerItemClick -> {
                when(action.type){
                    DrawerType.FixIssue -> {
                        _state.update { it.copy(
                            isShowBackgroundAccessBottomSheet = true
                        ) }
                    }
                    DrawerType.StepGoal -> {
                        _state.update { it.copy(
                            isShowStepGoalBottomSheet = true
                        ) }
                    }
                    DrawerType.Exit -> {
                        _state.update { it.copy(
                            isShowExitDialog = true
                        ) }
                    }

                    DrawerType.PersonalSettings -> {
                        viewModelScope.launch {
                            _uiEvent.send(DashboardEvent.NavigateToProfileSettings)
                        }
                    }
                }
            }

            DashboardAction.OnDismissExitDialog -> {
                _state.update { it.copy(
                    isShowExitDialog = false
                ) }
            }
            DashboardAction.OnExitOKClick -> Unit
            DashboardAction.OnShowStepGoalBottomSheet -> {
                _state.update { it.copy(
                    isShowStepGoalBottomSheet = true
                ) }
            }

            DashboardAction.OnDismissStepGoalBottomSheet -> {
                _state.update { it.copy(
                    isShowStepGoalBottomSheet = false
                ) }
            }
            is DashboardAction.OnStepGoalBottomSheetSaveClick ->{
                viewModelScope.launch {
                    val currentStepGoal = action.step
                    userDataStore.setStepGoal(step = currentStepGoal.toLongOrNull()?:2000)
                    _state.update { it.copy(
                        isShowStepGoalBottomSheet = false
                    ) }
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

    private fun observeUserStepGoal(){
        userDataStore
            .stepGoal
            .onEach {  stepGoal ->
                println("StepGoal: $stepGoal")
                _state.update { it.copy(
                    stepGoalPickerData = it.stepGoalPickerData.copy(
                        value = stepGoal.toString()
                    )
                ) }
            }
            .launchIn(viewModelScope)
    }


}
