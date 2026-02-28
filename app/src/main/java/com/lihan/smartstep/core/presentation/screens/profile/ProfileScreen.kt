@file:OptIn(ExperimentalMaterial3Api::class)

package com.lihan.smartstep.core.presentation.screens.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.lihan.smartstep.R
import com.lihan.smartstep.core.presentation.components.ModalListPicker
import com.lihan.smartstep.core.presentation.components.model.UnitType
import com.lihan.smartstep.core.presentation.design_system.PrimaryButton
import com.lihan.smartstep.ui.theme.NeutralLightLightest
import com.lihan.smartstep.ui.theme.TextPrimary

@Composable
fun ProfileScreen(
    state: ProfileState,
    onAction: (ProfileAction) -> Unit,
    modifier: Modifier = Modifier,
    header: @Composable () -> Unit = {},
    profileSettingSection: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    confirmButton: @Composable () -> Unit = {}
) {
    Column(
        modifier = modifier
    ) {
        CenterAlignedTopAppBar(
            modifier = Modifier.height(56.dp),
            windowInsets = WindowInsets(left = 16.dp, right = 16.dp),
            title = {
                Text(
                    text = stringResource(R.string.my_profile),
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary,
                    textAlign = TextAlign.Center
                )
            },
            actions = actions,
            colors = TopAppBarDefaults.topAppBarColors(containerColor = NeutralLightLightest)
        )
        header()
        profileSettingSection()

        Spacer(modifier = Modifier.weight(1f))
        confirmButton()
    }

    if (state.isShowHeightModal){
        ModalListPicker(
            title = stringResource(R.string.height),
            description = stringResource(R.string.used_to_calculate_distance),
            onDismiss = {
                onAction(ProfileAction.OnHeightModalDismiss)
            },
            onOkClick = {
                onAction(ProfileAction.OnHeightModalOkClick(it))
            },
            onCancelClick = {
                onAction(ProfileAction.OnHeightModalDismiss)
            },
            firstOption = UnitType.Cm,
            secondOption = UnitType.FtIn,
            items = if (state.selectHeightUnitTypeModal == UnitType.Cm){
                state.heightItems
            }else {
                state.heightFtItems
            },
            secondItems = if (state.selectHeightUnitTypeModal == UnitType.Cm){
                emptyList()
            }else {
                state.heightInchItems
            },
            value = if (state.selectHeightUnitTypeModal == UnitType.Cm){
                state.heightModalValue.toString()
            }else {
                state.heightModalValueFtIn.first.toString()
            },
            secondValue = if (state.selectHeightUnitTypeModal == UnitType.Cm){
                ""
            }else {
                state.heightModalValueFtIn.second.toString()
            },
            selectedOption = state.selectHeightUnitTypeModal,
            onUnitTypeClick = {
                onAction(ProfileAction.OnHeightUnitTypeClick(it))
            },
            onFirstItemChange = {
                onAction(ProfileAction.OnHeightModalFirstValueChange(it))
            },
            onSecondItemChange = {
                onAction(ProfileAction.OnHeightModalSecondValueChange(it))
            }
        )
    }

    if (state.isShowWeightModal){
        ModalListPicker(
            title = stringResource(R.string.weight),
            description = stringResource(R.string.used_to_calculate_distance),
            onDismiss = {
                onAction(ProfileAction.OnWeightModalDismiss)
            },
            onOkClick = {
                onAction(ProfileAction.OnWeightModalOkClick(it))
            },
            onCancelClick = {
                onAction(ProfileAction.OnWeightModalDismiss)
            },
            firstOption = UnitType.Kg,
            secondOption = UnitType.Lbs,
            items = state.weightItems,
            value = if (state.selectWeightUnitTypeModal == UnitType.Kg){
                state.weightModalValue.toString()
            }else{
                state.weightModalValueLbs.toString()
            },
            selectedOption = state.selectWeightUnitTypeModal,
            onUnitTypeClick = {
                onAction(ProfileAction.OnWeightUnitTypeClick(it))
            },
            onFirstItemChange = {
                onAction(ProfileAction.OnWeightModalFirstValueChange(it))
            },
            onSecondItemChange = {

            }
        )
    }

}