package com.lihan.smartstep.onboarding.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class ProfileViewModel: ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(ProfileState())
    val state = _state.onStart {
        if (!hasLoadedInitialData){

            hasLoadedInitialData = true
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        ProfileState()
    )

    fun onAction(action: ProfileAction){
        when(action){
            ProfileAction.OnGenderDropdownClick -> {
                _state.update { it.copy(
                    isExpandGender = true
                ) }
            }
            ProfileAction.OnGenderDropdownDismiss -> {
                _state.update { it.copy(
                    isExpandGender = false
                ) }
            }
            is ProfileAction.OnGenderItemSelected -> {
                _state.update { it.copy(
                    gender = action.gender,
                    isExpandGender = false
                ) }
            }
            ProfileAction.OnHeightModalDismiss -> {
                _state.update { it.copy(
                    isShowHeightModal = false
                ) }
            }
            is ProfileAction.OnHeightModalOkClick -> {
                _state.update { it.copy(
                    isShowHeightModal = false,
                    height = it.heightItems[action.index]
                ) }
            }
            ProfileAction.OnHeightModalShow -> {
                _state.update { it.copy(
                    isShowHeightModal = true
                )}
            }
            ProfileAction.OnSkipClick -> {
                //TODO: Skip Click
            }
            ProfileAction.OnWeightModalDismiss -> {
                _state.update { it.copy(
                    isShowWeightModal = false
                ) }
            }
            ProfileAction.OnWeightModalShow -> {
                _state.update { it.copy(
                    isShowWeightModal = true
                ) }
            }
            is ProfileAction.OnWeightModalOkClick -> {
                _state.update { it.copy(
                    isShowWeightModal = false,
                    weight = it.weightItems[action.index]
                ) }
            }
        }
    }
}