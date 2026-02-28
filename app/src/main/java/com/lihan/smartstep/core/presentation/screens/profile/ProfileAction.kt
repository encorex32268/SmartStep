package com.lihan.smartstep.core.presentation.screens.profile

import com.lihan.smartstep.core.presentation.components.model.UnitType
import com.lihan.smartstep.onboarding.presentation.model.Gender

sealed interface ProfileAction {
    data object OnSkipClick: ProfileAction
    data object OnGenderDropdownClick: ProfileAction
    data object OnGenderDropdownDismiss: ProfileAction
    data class OnGenderItemSelected(val gender: Gender): ProfileAction
    data object OnHeightModalShow: ProfileAction
    data object OnHeightModalDismiss: ProfileAction
    data class OnHeightModalOkClick(val value: String): ProfileAction
    data class OnHeightModalFirstValueChange(val value: String): ProfileAction
    data class OnHeightModalSecondValueChange(val value: String): ProfileAction
    data class OnHeightUnitTypeClick(val unitType: UnitType): ProfileAction
    data object OnWeightModalShow: ProfileAction
    data object OnWeightModalDismiss: ProfileAction
    data class OnWeightModalOkClick(val value: String): ProfileAction
    data class OnWeightModalFirstValueChange(val value: String): ProfileAction
    data class OnWeightUnitTypeClick(val unitType: UnitType): ProfileAction
    data object OnConfirmClick: ProfileAction

}