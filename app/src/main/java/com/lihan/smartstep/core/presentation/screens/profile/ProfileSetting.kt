package com.lihan.smartstep.core.presentation.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lihan.smartstep.R
import com.lihan.smartstep.core.presentation.components.model.UnitType
import com.lihan.smartstep.core.presentation.design_system.ArrowsDown
import com.lihan.smartstep.core.presentation.design_system.ArrowsUp
import com.lihan.smartstep.core.presentation.screens.profile.compoments.GenderDropdownMenu
import com.lihan.smartstep.core.presentation.screens.profile.compoments.InfoRow
import com.lihan.smartstep.onboarding.presentation.model.Gender
import com.lihan.smartstep.ui.theme.BackgroundSecondary
import com.lihan.smartstep.ui.theme.SmartStepTheme
import com.lihan.smartstep.ui.theme.TextPrimary

@Composable
fun ProfileSetting(
    gender: Gender,
    isExpandGender: Boolean,
    selectHeightUnitType: UnitType,
    selectWeightUnitType: UnitType,
    height: Int,
    heightFtIn: Pair<Int,Int>,
    weight: Int,
    weightLbs: Int,
    onGenderItemSelect: (Gender) -> Unit,
    onHeightModalShow: () -> Unit,
    onWeightModalShow: () -> Unit,
    onGenderDropMenuShow: () -> Unit,
    onDismissGenderDropMenu: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = BackgroundSecondary
){
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        GenderDropdownMenu(
            title = stringResource(R.string.gender),
            value = gender,
            isExpand = isExpandGender,
            onDropdownClick = onGenderDropMenuShow,
            onDismiss = onDismissGenderDropMenu,
            onItemSelect = {
                onGenderItemSelect(it)
            },
            containerColor = containerColor
        )
        InfoRow(
            title = stringResource(R.string.height),
            value = when(selectHeightUnitType){
                UnitType.Cm -> stringResource(R.string.height_cm,height)
                UnitType.FtIn ->  stringResource(R.string.height_ft_in,heightFtIn.first,heightFtIn.second)
                else ->  stringResource(R.string.height_cm,height)
            },
            onRowClick = onHeightModalShow,
            containerColor = containerColor,
            trailingIcon = {
                Icon(
                    imageVector = ArrowsDown,
                    tint = TextPrimary,
                    contentDescription = null
                )
            }
        )

        InfoRow(
            title = stringResource(R.string.weight),
            value = when(selectWeightUnitType){
                UnitType.Kg -> stringResource(R.string.weight_kg,weight)
                UnitType.Lbs -> stringResource(R.string.weight_lbs,weightLbs)
                else ->  stringResource(R.string.weight_kg,weight)
            },
            onRowClick = onWeightModalShow,
            containerColor = containerColor,
            trailingIcon = {
                Icon(
                    imageVector = ArrowsDown,
                    tint = TextPrimary,
                    contentDescription = null
                )
            }
        )

    }

}

@Preview(backgroundColor = 0xFFF9FAFB, showBackground = true)
@Composable
private fun ProfileScreenPreview() {
    SmartStepTheme {
        ProfileSetting(
            modifier = Modifier
                .background(BackgroundSecondary)
                .fillMaxWidth()
                .padding(16.dp),
            gender = Gender.FEMALE,
            selectHeightUnitType = UnitType.Cm,
            selectWeightUnitType = UnitType.Kg,
            height = 170,
            heightFtIn = Pair(5,9),
            weight = 60,
            weightLbs = 220,
            onHeightModalShow = {},
            onWeightModalShow = {},
            onGenderItemSelect = {},
            onGenderDropMenuShow = {},
            onDismissGenderDropMenu = {},
            isExpandGender = false
        )
    }
}