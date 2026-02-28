@file:OptIn(ExperimentalMaterial3Api::class)

package com.lihan.smartstep.onboarding.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lihan.smartstep.R
import com.lihan.smartstep.core.presentation.ObserveEvent
import com.lihan.smartstep.core.presentation.design_system.PrimaryButton
import com.lihan.smartstep.core.presentation.design_system.SmartStepTextButton
import com.lihan.smartstep.core.presentation.screens.profile.ProfileAction
import com.lihan.smartstep.core.presentation.screens.profile.ProfileScreen
import com.lihan.smartstep.core.presentation.screens.profile.ProfileSetting
import com.lihan.smartstep.core.presentation.screens.profile.ProfileState
import com.lihan.smartstep.core.presentation.screens.profile.ProfileUiEvent
import com.lihan.smartstep.core.presentation.screens.profile.ProfileViewModel
import com.lihan.smartstep.ui.theme.SmartStepTheme
import com.lihan.smartstep.ui.theme.StrokeMain
import com.lihan.smartstep.ui.theme.TextPrimary
import com.lihan.smartstep.ui.theme.TextWhite
import com.lihan.smartstep.ui.theme.bodyLargeRegular

@Composable
fun OnboardingProfileScreenRoot(
    onNavigateToSmartStep: () -> Unit,
    viewModel: ProfileViewModel,
    modifier: Modifier = Modifier
){
    val state by viewModel.state.collectAsStateWithLifecycle()
    ObserveEvent(viewModel.uiEvent) { event ->
        when(event){
            ProfileUiEvent.OnNavigateToSmartStep -> {
                onNavigateToSmartStep()
            }
        }
    }
    OnboardingProfileScreen(
        state = state,
        onAction = viewModel::onAction,
        modifier = modifier,
    )
}

@Composable
fun OnboardingProfileScreen(
    state: ProfileState,
    onAction: (ProfileAction) -> Unit,
    modifier: Modifier = Modifier,
){
    ProfileScreen(
        modifier = modifier,
        onAction = onAction,
        state = state,
        header = {
            Spacer(Modifier.height(32.dp))
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 36.dp),
                text = stringResource(R.string.my_profile_tips),
                style = MaterialTheme.typography.bodyLargeRegular,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )
        },
        actions = {
            SmartStepTextButton(
                text = stringResource(R.string.skip),
                onClick = {
                    onAction(ProfileAction.OnSkipClick)
                }
            )
        },
        confirmButton = {
            PrimaryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                text = stringResource(R.string.start),
                onClick = {
                    onAction(ProfileAction.OnConfirmClick)
                }
            )
        },
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
                    .padding(16.dp)
                    .clip(shape = RoundedCornerShape(14.dp))
                    .background(color = TextWhite, shape = RoundedCornerShape(14.dp))
                    .border(
                        width = 1.dp,
                        color = StrokeMain,
                        shape = RoundedCornerShape(14.dp)
                    )
                    .padding(16.dp)
            )
        }
    )
}



@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun ProfileScreenPreview() {
    SmartStepTheme {
        OnboardingProfileScreen(
            modifier = Modifier.safeDrawingPadding(),
            state = ProfileState(
                isShowHeightModal = false,
                isShowWeightModal = false,
                isExpandGender = false
            ),
            onAction = {}
        )
    }

}