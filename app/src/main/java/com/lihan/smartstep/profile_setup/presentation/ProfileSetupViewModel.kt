package com.lihan.smartstep.profile_setup.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.lihan.smartstep.core.data.model.Gender
import com.lihan.smartstep.core.data.model.HeightUnit
import com.lihan.smartstep.core.data.model.HeightUnit.Companion.formattedName
import com.lihan.smartstep.core.data.model.WeightUnit
import com.lihan.smartstep.core.data.model.WeightUnit.Companion.formattedName
import com.lihan.smartstep.core.domain.Route
import com.lihan.smartstep.core.domain.UserDataStore
import com.lihan.smartstep.core.presentation.components.WheelPickerData
import com.lihan.smartstep.profile_setup.presentation.model.feetInchesToCm
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class ProfileSetupViewModel(
    private val dataStore: UserDataStore
): ViewModel() {

    private var hasLoadedInitialData = false

    private val _uiEvent = Channel<ProfileSetupEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _state = MutableStateFlow(ProfileSetupState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                initData()
                observeOption()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ProfileSetupState()
        )

    fun onAction(action: ProfileSetupAction) {
        when (action) {
            ProfileSetupAction.OnDismissGenderDropdown -> {
                _state.update { it.copy(
                    isShowGenderDropdownMenu = false
                ) }
            }
            ProfileSetupAction.OnShowHeightDialog -> {
                _state.update { it.copy(
                    isShowHeightDialog = true,
                    heightItems = WheelPickerData.buildHeightWheelPickerDataListByUnit(
                        currentHeightValue = it.heightValue.roundToInt().toString(),
                        currentHeightItems = it.heightItems,
                        unit = it.heightUnitOption
                    ),
                ) }
            }
            ProfileSetupAction.OnDismissHeightDialog -> {
                _state.update { it.copy(
                    isShowHeightDialog = false
                ) }
            }
            ProfileSetupAction.OnShowWeightDialog -> {
                _state.update { it.copy(
                    isShowWeightDialog = true,
                    weightItems = WheelPickerData.buildWeightWheelPickerDataListByUnit(
                        currentWeight = it.weightValue.roundToInt().toString(),
                        currentWeightItems = it.weightItems,
                        unit = it.weightUnitOption
                    )
                ) }
            }
            ProfileSetupAction.OnDismissWeightDialog -> {
                _state.update { it.copy(
                    isShowWeightDialog = false
                ) }
            }
            ProfileSetupAction.OnGenderDropdownClick -> {
                _state.update { it.copy(
                    isShowGenderDropdownMenu = true
                ) }
            }
            is ProfileSetupAction.OnGenderSelected -> {
                viewModelScope.launch {
                    val userData = dataStore.userData.first() ?: return@launch
                    dataStore
                        .setUserData(
                            userData.copy(
                                gender = action.genderValue
                            )
                        )
                }
                _state.update { it.copy(
                    gender = Gender.fromName(action.genderValue),
                    isShowGenderDropdownMenu = false
                ) }
            }
            ProfileSetupAction.OnHeightDialogOkClick -> heightDialogConfirm()
            is ProfileSetupAction.OnHeightOptionClick -> heightOptionClick(action.index)
            ProfileSetupAction.OnWeightDialogOkClick -> weightDialogConfirm()
            is ProfileSetupAction.OnWeightOptionClick -> weightOptionClick(action.index)
            is ProfileSetupAction.OnHeightValue1Change -> heightValue1Change(action.value)
            is ProfileSetupAction.OnHeightValue2Change -> heightValue2Change(action.value)
            is ProfileSetupAction.OnWeightValueChange -> weightValueChange(action.value)
            ProfileSetupAction.OnSkipClick,
            ProfileSetupAction.OnStartClick -> navigateToDashboard()
            ProfileSetupAction.OnSaveClick -> navigateUp()
        }
    }

    private fun navigateToDashboard(){
        viewModelScope.launch {
            _uiEvent.send(ProfileSetupEvent.OnNavigateToDashboard)
        }
    }
    private fun navigateUp(){
        viewModelScope.launch {
            _uiEvent.send(ProfileSetupEvent.OnNavigateUp)
        }
    }


    private fun heightValue1Change(value: String){
        val currentState = state.value
        when(currentState.heightUnitOption){
            HeightUnit.Cm -> {
                _state.update { it.copy(
                    heightItems = it.heightItems.map { wheelPickerData ->
                        wheelPickerData.copy(
                            value = value
                        )
                    }
                ) }
            }
            HeightUnit.FtIn -> {
                _state.update { it.copy(
                    heightItems = it.heightItems.mapIndexed { index, wheelPickerData ->
                        if (index == 0){
                            wheelPickerData.copy(
                                value = value
                            )
                        }else{
                            wheelPickerData
                        }
                    }
                ) }
            }
        }
    }
    private fun heightValue2Change(value: String){
        _state.update { it.copy(
            heightItems = it.heightItems.mapIndexed { index, wheelPickerData ->
                if (index == 1){
                    wheelPickerData.copy(
                        value = value
                    )
                }else{
                    wheelPickerData
                }
            }
        ) }
    }
    private fun heightOptionClick(index: Int) {
        val option = HeightUnit.entries.getOrNull(index)?:return
        _state.update { it.copy(
            heightUnitOption = option
        ) }
    }
    private fun heightDialogConfirm() {
        viewModelScope.launch {
            val currentState = state.value
            val heightItems = currentState.heightItems
            val option = currentState.heightUnitOption

            val height = if (option == HeightUnit.Cm){
                heightItems[0].value
            }else{
                val feet = heightItems[0].value
                val inch = heightItems[1].value
                "$feet'$inch\"".feetInchesToCm()
            }

            val currentDataStore = dataStore.userData.first()?:return@launch
            dataStore.setUserData(
                currentDataStore.copy(
                    heightUnit = option.formattedName,
                    height = height?:"175"
                )
            )
            _state.update { it.copy(
                isShowHeightDialog = false
            ) }
        }



    }

    private fun weightValueChange(value: String){
        _state.update { it.copy(
            weightItems = it.weightItems.map { wheelPickerData ->
                wheelPickerData.copy(value = value)
            }
        ) }
    }

    private fun weightOptionClick(index: Int) {
        val option = WeightUnit.entries.getOrNull(index)?:return
        _state.update { it.copy(
            weightUnitOption = option
        ) }
    }

    private fun weightDialogConfirm() {
        viewModelScope.launch {
            val currentState = state.value
            val weightItems = currentState.weightItems
            val userData = dataStore.userData.first() ?: return@launch
            val option = currentState.weightUnitOption

            val weight = if (option == WeightUnit.Kg){
                weightItems[0].value.toDouble()
            }else{
                WeightUnit.Lbs.toKg(weightItems[0].value.toDouble())
            }

            dataStore.setUserData(
                data = userData.copy(
                    weight = weight.roundToInt().toString(),
                    weightUnit = option.formattedName
                )
            )
        }
        _state.update { it.copy(
            isShowWeightDialog = false
        ) }
    }

    private fun initData(){
        dataStore
            .userData
            .filterNotNull()
            .onEach { userData ->
                _state.update { it.copy(
                    gender = Gender.fromName(userData.gender),
                    heightValue = userData.height.toDouble(),
                    heightUnitOption = HeightUnit.fromName(userData.heightUnit),
                    weightValue = userData.weight.toDouble(),
                    weightUnitOption = WeightUnit.fromName(userData.weightUnit)
                ) }
            }
            .launchIn(viewModelScope)


    }


    private fun observeOption(){

        state.map { it.heightUnitOption }
            .distinctUntilChanged()
            .onEach { option ->
                val currentState = state.value
                val heightItems = currentState.heightItems
                val heightValue = currentState.heightValue

                val newHeightItems = WheelPickerData
                    .buildHeightWheelPickerDataListByUnit(
                        currentHeightValue = heightValue.roundToInt().toString(),
                        currentHeightItems = heightItems,
                        unit = option
                    )
                _state.update { it.copy(
                    heightItems = newHeightItems
                ) }
            }.launchIn(viewModelScope)

        state.map { it.weightUnitOption }
            .distinctUntilChanged()
            .onEach { option ->
                val currentState = state.value
                val weightItems = currentState.weightItems
                val weightValue = currentState.weightValue

                val newWeightItems = WheelPickerData
                    .buildWeightWheelPickerDataListByUnit(
                        currentWeight = weightValue.roundToInt().toString(),
                        currentWeightItems = weightItems,
                        unit = option
                    )
                _state.update { it.copy(
                    weightItems = newWeightItems
                ) }
            }
            .launchIn(viewModelScope)
    }

}