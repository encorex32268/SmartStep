package com.lihan.smartstep.stepcount.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class SmartStepViewModel: ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(SmartStepState())
    val state = _state
        .onEach { it ->
            println("state ${it}")
        }
        .onStart {
        if (!hasLoadedInitialData){

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
            SmartStepAction.OnStepGoalSaveClick -> saveStepGoal()
            SmartStepAction.OnExitClick -> Unit
            SmartStepAction.OnMenuClick -> Unit
            SmartStepAction.OnPersonSettingsClick -> Unit
            SmartStepAction.OnShowBackgroundAccessModal -> TODO()
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
            is SmartStepAction.OnStepGoalValueChanged -> TODO()
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
        }
    }

    private fun saveStepGoal() {
        //TODO: SaveStepGoal
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
}