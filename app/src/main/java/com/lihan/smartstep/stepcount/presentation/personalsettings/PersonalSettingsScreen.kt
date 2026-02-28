@file:OptIn(ExperimentalMaterial3Api::class)

package com.lihan.smartstep.stepcount.presentation.personalsettings

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lihan.smartstep.R
import com.lihan.smartstep.core.presentation.ObserveEvent
import com.lihan.smartstep.core.presentation.design_system.PrimaryButton
import com.lihan.smartstep.core.presentation.screens.profile.ProfileAction
import com.lihan.smartstep.core.presentation.screens.profile.ProfileScreen
import com.lihan.smartstep.core.presentation.screens.profile.ProfileSetting
import com.lihan.smartstep.core.presentation.screens.profile.ProfileState
import com.lihan.smartstep.core.presentation.screens.profile.ProfileUiEvent
import com.lihan.smartstep.core.presentation.screens.profile.ProfileViewModel
import com.lihan.smartstep.ui.theme.BackgroundWhite
import com.lihan.smartstep.ui.theme.TextPrimary
import com.lihan.smartstep.ui.theme.bodyLargeMedium
import org.koin.androidx.compose.koinViewModel

@Composable
fun PersonalSettingsScreenRoot(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = koinViewModel(),
){
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveEvent(viewModel.uiEvent) { event ->
        when(event){
            ProfileUiEvent.OnNavigateToSmartStep -> {
                onBack()
            }
        }
    }

    PersonalSettingsScreen(
        state = state,
        onAction = viewModel::onAction,
        modifier = modifier
    )
}

@Composable
fun PersonalSettingsScreen(
    state: ProfileState,
    onAction: (ProfileAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    ProfileScreen(
        state = state,
        header = {
            Spacer(Modifier.height(32.dp))
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                text = stringResource(R.string.personal_settings_header),
                style = MaterialTheme.typography.bodyLargeMedium,
                color = TextPrimary,
            )
        },
        actions = {},
        profileSettingSection = {
            ProfileSetting(
                gender = state.gender,
                height = state.height,
                heightFtIn = state.heightFtIn,
                weight = state.weight,
                weightLbs = state.weightLbs,
                selectWeightUnitType = state.selectWeightUnitType,
                selectHeightUnitType = state.selectHeightUnitType,
                onHeightModalShow = {
                    onAction(ProfileAction.OnHeightModalShow)
                },
                onWeightModalShow = {
                    onAction(ProfileAction.OnWeightModalShow)
                },
                onGenderItemSelect = { gender ->
                    onAction(ProfileAction.OnGenderItemSelected(gender))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                containerColor = BackgroundWhite,
                onDismissGenderDropMenu = {
                    onAction(ProfileAction.OnDismissGenderDropMenu)
                },
                onGenderDropMenuShow = {
                    onAction(ProfileAction.OnGenderDropMenuShow)
                },
                isExpandGender = state.isExpandGender
            )
        },
        confirmButton = {
            PrimaryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                text = stringResource(R.string.save),
                onClick = {
                    onAction(ProfileAction.OnConfirmClick)
                }
            )
        },
        onAction = onAction,
        modifier = modifier,
    )

}


@Preview(showBackground = true)
@Composable
private fun PersonalSettingsScreenPreview() {

    PersonalSettingsScreen(
        modifier = Modifier.safeDrawingPadding(),
        state = ProfileState(
            isShowHeightModal = false,
            isShowWeightModal = false,
            isExpandGender = false
        ),
        onAction = {}
    )
}