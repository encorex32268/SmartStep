package com.lihan.smartstep.profile_setup.presentation

sealed interface ProfileSetupAction {
    data object OnSkipClick: ProfileSetupAction
    data object OnGenderDropdownClick: ProfileSetupAction
    data object OnDismissGenderDropdown: ProfileSetupAction
    data class OnGenderSelected(val genderValue: String): ProfileSetupAction
    data object OnShowHeightDialog: ProfileSetupAction
    data object OnDismissHeightDialog: ProfileSetupAction
    data object OnHeightDialogOkClick: ProfileSetupAction
    data object OnShowWeightDialog: ProfileSetupAction
    data class OnHeightOptionClick(val index: Int): ProfileSetupAction
    data class OnWeightOptionClick(val index: Int): ProfileSetupAction
    data object OnDismissWeightDialog: ProfileSetupAction
    data object OnWeightDialogOkClick: ProfileSetupAction
    data class OnHeightValue1Change(val value: String): ProfileSetupAction
    data class OnHeightValue2Change(val value: String): ProfileSetupAction
    data class OnWeightValueChange(val value: String): ProfileSetupAction

}