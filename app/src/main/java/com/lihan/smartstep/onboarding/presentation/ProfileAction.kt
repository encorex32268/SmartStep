package com.lihan.smartstep.onboarding.presentation

import com.lihan.smartstep.onboarding.presentation.model.Gender

sealed interface ProfileAction {
    data object OnSkipClick: ProfileAction
    data object OnGenderDropdownClick: ProfileAction
    data object OnGenderDropdownDismiss: ProfileAction
    data class OnGenderItemSelected(val gender: Gender): ProfileAction
    data object OnHeightModalShow: ProfileAction
    data object OnHeightModalDismiss: ProfileAction
    data class OnHeightModalOkClick(val index: Int): ProfileAction
    data object OnWeightModalShow: ProfileAction
    data object OnWeightModalDismiss: ProfileAction
    data class OnWeightModalOkClick(val index: Int): ProfileAction
}