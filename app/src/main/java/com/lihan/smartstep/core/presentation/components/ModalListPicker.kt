@file:OptIn(ExperimentalMaterial3Api::class)

package com.lihan.smartstep.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lihan.smartstep.R
import com.lihan.smartstep.core.presentation.components.model.UnitType
import com.lihan.smartstep.core.presentation.components.model.UnitType.Companion.isNeedTwoColumn
import com.lihan.smartstep.core.presentation.design_system.SmartStepTextButton
import com.lihan.smartstep.core.presentation.modifier.negativePadding
import com.lihan.smartstep.ui.theme.BackgroundSecondary
import com.lihan.smartstep.ui.theme.BackgroundTertiary
import com.lihan.smartstep.ui.theme.SmartStepTheme
import com.lihan.smartstep.ui.theme.TextPrimary
import com.lihan.smartstep.ui.theme.TextSecondary
import com.lihan.smartstep.ui.theme.bodyMediumRegular

@Composable
fun ModalListPicker(
    title: String,
    description: String,
    value: String,
    items: List<String>,
    firstOption: UnitType,
    onFirstItemChange: (String) -> Unit,
    onSecondItemChange: (String) -> Unit,
    selectedOption: UnitType,
    secondOption: UnitType,
    onUnitTypeClick: (UnitType) -> Unit,
    onOkClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onCancelClick: () -> Unit,
    secondValue: String = "",
    secondItems: List<String> = emptyList()
) {

    var firstItemValue by rememberSaveable {
        mutableStateOf(value)
    }

    var secondItemValue by rememberSaveable {
        mutableStateOf(secondValue)
    }

    val ftInOfFt = stringResource(R.string.ft_in_ft)
    val ftInOfIn = stringResource(R.string.ft_in_in)

    AdaptiveModal(
        modifier = modifier,
        isDialogLayout = true,
        dragHandle = null,
        onDismiss = onDismiss,
        content = {
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(28.dp))
                    .background(color = BackgroundSecondary)
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMediumRegular,
                    color = TextSecondary
                )
                Spacer(Modifier.height(20.dp))
                Switcher(
                    firstOption = firstOption,
                    secondOption = secondOption,
                    selectedOption = selectedOption,
                    onClick = onUnitTypeClick,
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .negativePadding(24.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .padding(bottom = 44.dp)
                            .fillMaxWidth()
                            .height(44.dp)
                            .background(color = BackgroundTertiary)
                            .align(Alignment.BottomStart)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth().height(176.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        if (selectedOption.isNeedTwoColumn) {
                            WheelPicker(
                                items = items,
                                value = value,
                                onValueChange = {
                                    firstItemValue = items[it]
                                    onFirstItemChange(items[it])
                                },
                                modifier = Modifier.weight(1f)
                            )
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                            ){
                                Spacer(Modifier.weight(2f))
                                Box(
                                    modifier = Modifier.weight(1f),
                                    contentAlignment = Alignment.Center
                                ){
                                    Text(
                                        modifier = Modifier.fillMaxWidth(),
                                        text = stringResource(R.string.ft_in_ft),
                                        style = MaterialTheme.typography.titleMedium,
                                        color = TextPrimary,
                                        textAlign = TextAlign.Center
                                    )
                                }
                                Spacer(Modifier.weight(1f))
                            }
                            WheelPicker(
                                items = secondItems,
                                value = secondValue,
                                onValueChange = {
                                    secondItemValue = secondItems[it]
                                    onSecondItemChange(secondItems[it])
                                },
                                modifier = Modifier.weight(1f)
                            )
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                            ){
                                Spacer(Modifier.weight(2f))
                                Box(
                                    modifier = Modifier.weight(1f),
                                    contentAlignment = Alignment.Center
                                ){
                                    Text(
                                        modifier = Modifier.fillMaxWidth(),
                                        text = stringResource(R.string.ft_in_in),
                                        style = MaterialTheme.typography.titleMedium,
                                        color = TextPrimary,
                                        textAlign = TextAlign.Center
                                    )
                                }
                                Spacer(Modifier.weight(1f))
                            }
                        } else {
                            WheelPicker(
                                items = items,
                                value = value,
                                onValueChange = {
                                    firstItemValue = items[it]
                                    onFirstItemChange(items[it])
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
                ) {
                    SmartStepTextButton(
                        text = stringResource(R.string.cancel),
                        onClick = onCancelClick
                    )
                    SmartStepTextButton(
                        text = stringResource(R.string.ok),
                        onClick = {
                            val itemString = if (selectedOption.isNeedTwoColumn) {
                                firstItemValue + ftInOfFt + secondItemValue + ftInOfIn
                            } else {
                                firstItemValue
                            }
                            onOkClick(itemString)
                        }
                    )
                }
            }
        }
    )

}


@Preview(showBackground = true)
@Composable
private fun ListPickerPreview() {
    SmartStepTheme {
        ModalListPicker(
            title = stringResource(R.string.height),
            description = stringResource(R.string.used_to_calculate_distance),
            value = "150",
            secondValue = "150",
            onDismiss = {},
            onOkClick = {},
            onCancelClick = {},
//            firstOption = UnitType.Cm,
            firstOption = UnitType.Cm,
            secondOption = UnitType.FtIn,
            selectedOption = UnitType.FtIn,
            items = (0..30).map {
                "${145 + it}"
            },
            onUnitTypeClick = {},
//            secondItems = (0..30).map {
            secondItems = (0..30).map {
                "${145 + it}"
            },
            onFirstItemChange = {},
//            onSecondItemChange = {}
            onSecondItemChange = {}
        )
    }
}