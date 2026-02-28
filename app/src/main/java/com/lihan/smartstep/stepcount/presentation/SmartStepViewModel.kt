package com.lihan.smartstep.stepcount.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lihan.smartstep.core.domain.UserInfoDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SmartStepViewModel(
    private val userInfoDataStore: UserInfoDataStore
): ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(SmartStepState())
    val state = _state
        .onStart {
        if (!hasLoadedInitialData){
            observeTotalStep()
            initBackgroundAccess()
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
            SmartStepAction.OnExitClick -> {
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
            SmartStepAction.OnShowExitModal -> {
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
}