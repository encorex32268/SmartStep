package com.lihan.smartstep.core.presentation.screens.profile

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lihan.smartstep.core.domain.UserInfoDataStore
import com.lihan.smartstep.core.presentation.components.model.UnitType
import com.lihan.smartstep.onboarding.presentation.model.Gender
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class ProfileViewModel(
    private val userInfoDataStore: UserInfoDataStore
): ViewModel() {

    private var hasLoadedInitialData = false

    private val _uiEvent = Channel<ProfileUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _state = MutableStateFlow(ProfileState())
    val state = _state.onStart {
        if (!hasLoadedInitialData){
            initData()
            hasLoadedInitialData = true
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        ProfileState()
    )

    fun onAction(action: ProfileAction){
        when(action){
            ProfileAction.OnGenderDropdownClick -> showGenderDropdown()
            ProfileAction.OnGenderDropdownDismiss -> dismissGenderDropdown()
            is ProfileAction.OnGenderItemSelected -> onGenderItemSelected(action.gender)
            ProfileAction.OnHeightModalDismiss -> dismissHeightModal()
            is ProfileAction.OnHeightModalOkClick -> onHeightConfirm(action.value)
            ProfileAction.OnHeightModalShow -> showHeightModal()
            ProfileAction.OnWeightModalDismiss -> dismissWeightModal()
            ProfileAction.OnWeightModalShow -> showWeightModal()
            is ProfileAction.OnWeightModalOkClick -> onWeightConfirm(action.value)
            is ProfileAction.OnHeightUnitTypeClick -> onHeightUnitTypeChange(action.unitType)
            is ProfileAction.OnWeightUnitTypeClick -> onWeightUnitTypeChange(action.unitType)
            ProfileAction.OnConfirmClick -> saveProfile()
            is ProfileAction.OnHeightModalFirstValueChange -> onHeightModalFirstValueChange(action.value)
            is ProfileAction.OnHeightModalSecondValueChange -> onHeightModalSecondValueChange(action.value)
            is ProfileAction.OnWeightModalFirstValueChange -> onWeightModalFirstValueChange(action.value)
            else -> Unit
        }
    }

    private fun initData(){
        viewModelScope.launch {
            combine(
                flow = userInfoDataStore.getGender(),
                flow2 = userInfoDataStore.getHeight(),
                flow3 = userInfoDataStore.getWeight()
            ){ gender,height,weight ->

                _state.update { it.copy(
                    gender = gender,
                    height = height,
                    heightModalValue = height,
                    weight = weight,
                    weightModalValue = weight
                ) }

            }.first()
        }
    }

    private fun onWeightModalFirstValueChange(value: String) {
        val currentWeightUnitType = state.value.selectWeightUnitType

        val newValue = if (currentWeightUnitType == UnitType.Kg){
            value.toInt()
        }else{
            (value.toFloat() / 2.20462).roundToInt()
        }


        _state.update { it.copy(
            weightModalValue = newValue
        ) }
    }

    private fun onHeightModalSecondValueChange(value: String) {
        val currentHeightUnitType = state.value.selectHeightUnitType
        if (currentHeightUnitType == UnitType.Cm) return

        val ft = state.value.heightModalValueFtIn.first
        val inch = value.toInt()

        val newValue = (((ft * 12) + inch)*2.54f).roundToInt().toString()

        _state.update { it.copy(
            heightModalValue = newValue.toInt()
        ) }

    }

    private fun onHeightModalFirstValueChange(value: String) {
        val currentState = state.value
        val currentHeightUnitType = currentState.selectHeightUnitType
        val newValue = if (currentHeightUnitType == UnitType.Cm){
            value.toInt()
        }else{
            val ft = value.toInt()
            val inch = currentState.heightModalValueFtIn.second
            (((ft * 12) + inch)*2.54f).roundToInt()
        }
        _state.update { it.copy(
            heightModalValue = newValue,
        ) }
    }


    private fun onHeightConfirm(value: String) {
        val currentHeightUnitType = state.value.selectHeightUnitType
        val newValue = if (currentHeightUnitType == UnitType.Cm && value.isDigitsOnly()){
            value.toInt()
        }else{
            val ft = value.substringBefore("ft").toIntOrNull()?:0
            val inch = value
                .substringAfter("ft")
                .substringBefore("in")
                .toIntOrNull()?:0
            (((ft * 12) + inch)*2.54f).roundToInt()
        }

        _state.update { it.copy(
            isShowHeightModal = false,
            height = newValue,
            selectHeightUnitType = it.selectHeightUnitTypeModal
        ) }
    }



    private fun onWeightConfirm(weight: String){
        val newWeight = if (state.value.selectWeightUnitType == UnitType.Kg){
            weight.toIntOrNull()?:60
        }else{
            (weight.toFloat() * 2.20462).roundToInt()
        }
        _state.update { it.copy(
            isShowWeightModal = false,
            weight = newWeight,
            selectWeightUnitType = it.selectWeightUnitTypeModal
        ) }
    }

    private fun saveProfile() {
        viewModelScope.launch {
            val currentState = state.value
            val genderUpdateJob = async { userInfoDataStore.updateGender(currentState.gender) }
            val heightUpdateJob = async {
                userInfoDataStore.updateHeight(currentState.height)
            }
            val weightUpdateJob = async {
                val currentWeight = if (currentState.selectWeightUnitType == UnitType.Lbs){
                    (currentState.weight / 2.20462 ).roundToInt()
                }else{
                    currentState.weight
                }
                userInfoDataStore.updateWeight(currentWeight)
            }
            awaitAll(genderUpdateJob,heightUpdateJob,weightUpdateJob)

            _uiEvent.send(
                ProfileUiEvent.OnNavigateToSmartStep
            )
        }
    }



    private fun onGenderItemSelected(gender: Gender) {
        _state.update { it.copy(
            gender = gender,
            isExpandGender = false
        ) }
    }

    private fun showGenderDropdown() {
        _state.update { it.copy(
            isExpandGender = true
        ) }
    }

    private fun dismissGenderDropdown() {
        _state.update { it.copy(
            isExpandGender = false
        ) }
    }

    private fun onHeightUnitTypeChange(unitType: UnitType) {
        _state.update { it.copy(
            selectHeightUnitTypeModal = unitType
        ) }
    }

    private fun onWeightUnitTypeChange(unitType: UnitType) {
        _state.update { it.copy(
            selectWeightUnitTypeModal = unitType
        ) }
    }

    private fun showHeightModal() {
        _state.update { it.copy(
            isShowHeightModal = true,
            heightModalValue = it.height
        )}
    }

    private fun dismissHeightModal() {
        _state.update { it.copy(
            isShowHeightModal = false,
            heightModalValue = it.height
        ) }
    }

    private fun showWeightModal(){
        _state.update { it.copy(
            isShowWeightModal = true,
            weightModalValue = it.weight
        ) }
    }

    private fun dismissWeightModal() {
        _state.update { it.copy(
            isShowWeightModal = false,
            weightModalValue = it.weight
        ) }
    }

}