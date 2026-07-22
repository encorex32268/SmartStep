@file:OptIn(ExperimentalMaterial3Api::class)

package com.lihan.smartstep.profile_setup.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lihan.smartstep.R
import com.lihan.smartstep.core.data.model.Gender
import com.lihan.smartstep.core.data.model.HeightUnit
import com.lihan.smartstep.core.data.model.HeightUnit.Companion.formattedName
import com.lihan.smartstep.core.data.model.WeightUnit
import com.lihan.smartstep.core.data.model.WeightUnit.Companion.formattedName
import com.lihan.smartstep.core.presentation.components.ProfileWheelPickerDialog
import com.lihan.smartstep.core.presentation.components.SettingsDropdown
import com.lihan.smartstep.core.presentation.components.SettingsField
import com.lihan.smartstep.core.presentation.components.SettingsWheelPicker
import com.lihan.smartstep.core.presentation.components.SingleValueWheelPicker
import com.lihan.smartstep.core.presentation.design_system.buttons.ButtonType
import com.lihan.smartstep.core.presentation.design_system.buttons.SmartStepButton
import com.lihan.smartstep.core.presentation.design_system.topbar.SmartStepTopbar
import com.lihan.smartstep.core.presentation.ui.theme.BackgroundWhite
import com.lihan.smartstep.core.presentation.ui.theme.SmartStepTheme
import com.lihan.smartstep.core.presentation.util.ObserveAsEvents
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileSetupRoot(
    onNavigateToDashboard: () -> Unit,
    viewModel: ProfileSetupViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.uiEvent){ event ->
        when(event){
            ProfileSetupEvent.OnNavigateToDashboard -> onNavigateToDashboard()
        }
    }

    ProfileSetupScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun ProfileSetupScreen(
    state: ProfileSetupState,
    onAction: (ProfileSetupAction) -> Unit,
) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            SmartStepTopbar(
                title = stringResource(R.string.my_profile),
                actions = {
                    SmartStepButton(
                        text = stringResource(R.string.skip),
                        type = ButtonType.Text,
                        onClick = {
                            onAction(ProfileSetupAction.OnSkipClick)
                        }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BackgroundWhite,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(32.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.my_profile_information),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(16.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        color = BackgroundWhite,
                        shape = RoundedCornerShape(14.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(14.dp)
                    )
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SettingsDropdown(
                    title = stringResource(R.string.gender),
                    value = state.gender.name,
                    isDropdown = state.isShowGenderDropdownMenu,
                    dropDownItems = Gender.entries.map { it.name },
                    onDropdownClick = {
                        onAction(ProfileSetupAction.OnGenderDropdownClick)
                    },
                    onDismissRequest = {
                        onAction(ProfileSetupAction.OnDismissGenderDropdown)
                    },
                    onItemClick = { genderString ->
                        onAction(ProfileSetupAction.OnGenderSelected(genderString))
                    }
                )
                SettingsField(
                    title = stringResource(R.string.height),
                    value = when(state.heightUnitOption){
                        HeightUnit.Cm -> stringResource(R.string.height_cm,state.displayHeightString)
                        HeightUnit.FtIn -> state.displayHeightString
                    },
                    onFieldClick = {
                        onAction(ProfileSetupAction.OnShowHeightDialog)
                    }
                )
                SettingsField(
                    title = stringResource(R.string.weight),
                    value = when(state.weightUnitOption){
                        WeightUnit.Kg -> stringResource(R.string.weight_kg,state.displayWeightString)
                        WeightUnit.Lbs -> stringResource(R.string.weight_lbs,state.displayWeightString)
                    },
                    onFieldClick = {
                        onAction(ProfileSetupAction.OnShowWeightDialog)
                    }
                )
            }
            Spacer(Modifier.weight(1f))
            SmartStepButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                text = stringResource(R.string.start),
                onClick = {

                }
            )


        }

    }

    if (state.isShowHeightDialog){
        ProfileWheelPickerDialog(
            title = stringResource(R.string.height),
            description = stringResource(R.string.height_description),
            options = HeightUnit.lowercaseUnits,
            onCancelClick = {
                onAction(ProfileSetupAction.OnDismissHeightDialog)
            },
            onOkClick = {
                onAction(ProfileSetupAction.OnHeightDialogOkClick)
            },
            onOptionClick = { index ->
                onAction(ProfileSetupAction.OnHeightOptionClick(index))
            },
            selectOption = state.heightUnitOption.formattedName,
            content = {
                SettingsWheelPicker(
                    data = state.heightItems,
                    onValue1Change = {
                        onAction(ProfileSetupAction.OnHeightValue1Change(it))
                    },
                    onValue2Change = {
                        onAction(ProfileSetupAction.OnHeightValue2Change(it))
                    },
                )
            },
        )
    }

    if (state.isShowWeightDialog){
        ProfileWheelPickerDialog(
            title = stringResource(R.string.weight),
            description = stringResource(R.string.weight_description),
            options = WeightUnit.lowercaseUnits,
            onCancelClick = {
                onAction(ProfileSetupAction.OnDismissWeightDialog)
            },
            onOkClick = {
                onAction(ProfileSetupAction.OnWeightDialogOkClick)
            },
            onOptionClick = { index ->
                onAction(ProfileSetupAction.OnWeightOptionClick(index))
            },
            selectOption = state.weightUnitOption.formattedName,
            content = {
                SettingsWheelPicker(
                    data = state.weightItems,
                    onValue1Change = {
                        onAction(ProfileSetupAction.OnWeightValueChange(it))
                    }
                )
            },
        )
    }



}

@Preview
@Composable
private fun Preview() {
    SmartStepTheme {
        ProfileSetupScreen(
            state = ProfileSetupState(
                isShowHeightDialog = true
            ),
            onAction = {}
        )
    }
}