package com.lihan.smartstep.dashboard.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lihan.smartstep.core.domain.UserDataStore
import com.lihan.smartstep.dashboard.domain.AppPowerManager
import com.lihan.smartstep.dashboard.presentation.components.DrawerType
import kotlinx.coroutines.Job
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
        }
    }

    private fun resetTodaySteps(){
        //TODO: Reset today's steps
        _state.update { it.copy(
            isShowResetDialog = false
        ) }
    }

    private fun dismissResetTodayDialog(){
        _state.update { it.copy(
            isShowResetDialog = false
        ) }
    }

    private fun saveEditSteps(){
        val currentState = state.value
        val editDate = currentState.dateTime
        val editSteps = currentState.editStepsTextFieldState.text.toString()
        //TODO: Update db

        _state.update { it.copy(
            isShowEditStepsDialog = false
        ) }
    }


    private fun dismissEditStepsDialog(){
        _state.update { it.copy(
            isShowEditStepsDialog = false
        ) }
    }

    private fun updateEditStepsDate(time: Long){
        _state.update { it.copy(
            dateTime = time,
            isShowDatePickerDialog = false
        ) }
    }

    private fun showDatePickerDialog(){
        _state.update { it.copy(
            isShowDatePickerDialog = true
        )}
    }

    private fun dismissDatePickerDialog(){
        _state.update { it.copy(
            isShowDatePickerDialog = false
        )}
    }

    private fun saveStepGoal(step: String) {
        viewModelScope.launch {
            userDataStore.setStepGoal(step = step.toLongOrNull()?:2000)
            _state.update { it.copy(
                isShowStepGoalBottomSheet = false
            ) }
        }
    }

    private fun dismissStepGoalBottomSheet() {
        _state.update { it.copy(
            isShowStepGoalBottomSheet = false
        ) }
    }

    private fun showStepGoalBottomSheet(){
        _state.update { it.copy(
            isShowStepGoalBottomSheet = true
        ) }
    }

    private fun dismissExitDialog(){
        _state.update { it.copy(
            isShowExitDialog = false
        ) }
    }

    private fun backgroundAccessContinue() {
        _state.update { it.copy(
           isShowBackgroundAccessBottomSheet = false
        ) }
    }

    private fun showEnableAccessManuallyBottomSheet(){
        _state.update { it.copy(
            isShowAllowAccessBottomSheet = false,
            isShowEnableAccessManuallyBottomSheet = true
        ) }
    }

    private fun showAllowAccessBottomSheet(){
        _state.update { it.copy(
            isShowAllowAccessBottomSheet = true
        ) }
    }

    private fun showBackgroundAccessBottomSheet(){
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
        when(type){
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
            DrawerType.EditSteps -> {
                _state.update { it.copy(
                    isShowEditStepsDialog = true
                ) }
            }
            DrawerType.RestTodaySteps -> {
                _state.update { it.copy(
                    isShowResetDialog = true
                ) }
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
                _state.update { it.copy(
                    stepGoalPickerData = it.stepGoalPickerData.copy(
                        value = stepGoal.toString()
                    )
                ) }
            }
            .launchIn(viewModelScope)
    }


}
