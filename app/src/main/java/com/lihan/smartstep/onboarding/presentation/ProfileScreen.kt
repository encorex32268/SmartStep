@file:OptIn(ExperimentalMaterial3Api::class)

package com.lihan.smartstep.onboarding.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lihan.smartstep.R
import com.lihan.smartstep.core.presentation.components.ModalListPicker
import com.lihan.smartstep.core.presentation.design_system.PrimaryButton
import com.lihan.smartstep.core.presentation.design_system.SmartStepTextButton
import com.lihan.smartstep.onboarding.presentation.model.HeightUnit
import com.lihan.smartstep.onboarding.presentation.model.WeightUnit
import com.lihan.smartstep.profile.presentation.GenderDropdownMenu
import com.lihan.smartstep.profile.presentation.InfoRow
import com.lihan.smartstep.ui.theme.SmartStepTheme
import com.lihan.smartstep.ui.theme.StrokeMain
import com.lihan.smartstep.ui.theme.TextPrimary
import com.lihan.smartstep.ui.theme.TextWhite
import com.lihan.smartstep.ui.theme.bodyLargeRegular

@Composable
fun ProfileScreenRoot(
    viewModel: ProfileViewModel,
    modifier: Modifier = Modifier
){
    val state by viewModel.state.collectAsStateWithLifecycle()

    ProfileScreen(
        state = state,
        onAction = viewModel::onAction,
        modifier = modifier
    )
}

@Composable
private fun ProfileScreen(
    state: ProfileState,
    onAction: (ProfileAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.my_profile),
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )
            },
            actions = {
                SmartStepTextButton(
                    text = stringResource(R.string.skip),
                    onClick = {
                        //TODO: Skip
                    }
                )
            }
        )
        Spacer(Modifier.height(32.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 36.dp)
                .align(Alignment.CenterHorizontally),
            text = stringResource(R.string.my_profile_tips),
            style = MaterialTheme.typography.bodyLargeRegular,
            color = TextPrimary,
            textAlign = TextAlign.Center
        )
        Column(
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            GenderDropdownMenu(
                title = stringResource(R.string.gender),
                value = state.gender,
                isExpand = state.isExpandGender,
                onDropdownClick = {
                    onAction(ProfileAction.OnGenderDropdownClick)
                },
                onDismiss = {
                    onAction(ProfileAction.OnGenderDropdownDismiss)
                },
                onItemSelect = {
                    onAction(ProfileAction.OnGenderItemSelected(it))
                }
            )

            InfoRow(
                title = stringResource(R.string.height),
                value = if (state.heightUnit == HeightUnit.CM){
                    stringResource(R.string.height_cm,state.height)
                }else{
                    stringResource(R.string.height_ft_in,state.height)
                },
                onRowClick = {
                    onAction(ProfileAction.OnHeightModalShow)
                }
            )

            InfoRow(
                title = stringResource(R.string.weight),
                value = if (state.weightUnit == WeightUnit.KG){
                    stringResource(R.string.weight_kg,state.weight)
                }else{
                    stringResource(R.string.weight_lbs,state.weight)
                },
                onRowClick = {
                    onAction(ProfileAction.OnWeightModalShow)
                }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        PrimaryButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            text = stringResource(R.string.start),
            onClick = {}
        )
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
            firstText = stringResource(R.string.cm),
            secondText = stringResource(R.string.ft_in),
            items = state.heightItems,
            value = state.height.toString(),
            selectUnit = when(state.heightUnit){
                HeightUnit.CM -> stringResource(R.string.cm)
                HeightUnit.FI_IN -> stringResource(R.string.ft_in)
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
            firstText = stringResource(R.string.kg),
            secondText = stringResource(R.string.lbs),
            items = state.weightItems,
            value = state.height.toString(),
            selectUnit = when(state.weightUnit){
                WeightUnit.KG -> stringResource(R.string.kg)
                WeightUnit.LBS -> stringResource(R.string.lbs)
            }
        )
    }

}

@Preview(
    showBackground = true
)
@Composable
private fun ProfileScreenPreview() {
    SmartStepTheme {
        ProfileScreen(
            state = ProfileState(
                isShowHeightModal = false,
                heightUnit = HeightUnit.FI_IN,
                isShowWeightModal = true
            ),
            onAction = {}
        )
    }

}