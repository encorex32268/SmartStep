package com.lihan.smartstep.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.lihan.smartstep.ui.theme.BackgroundSecondary
import com.lihan.smartstep.ui.theme.SmartStepTheme
import com.lihan.smartstep.ui.theme.TextPrimary
import com.lihan.smartstep.ui.theme.TextSecondary
import com.lihan.smartstep.ui.theme.bodyMediumRegular
import kotlinx.coroutines.launch

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
    val scope = rememberCoroutineScope()

    val listState = key(items) {
        rememberLazyListState(
            initialFirstVisibleItemIndex = items.indexOf(value).coerceIn(0, items.lastIndex)
        )
    }

    val ftInOfFt = stringResource(R.string.ft_in_ft)
    val ftInOfIn = stringResource(R.string.ft_in_in)

    var firstCenterValue by rememberSaveable {
        mutableIntStateOf(0)
    }

    var secondCenterValue by rememberSaveable {
        mutableIntStateOf(0)
    }


    val secondListState = key(secondItems) {
        rememberLazyListState(
            initialFirstVisibleItemIndex = if (secondItems.isEmpty()){ 0 } else {
                secondItems.indexOf(secondValue).coerceIn(0, secondItems.lastIndex)
            }
        )
    }



    AdaptiveModal(
        modifier = modifier,
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
                Spacer(Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    //when user select ft/in
                    if (selectedOption.isNeedTwoColumn){
                        WheelPicker(
                            modifier = Modifier.weight(1f),
                            listState = listState,
                            items = items,
                            onValueChanged = onFirstItemChange,
                            itemContent = { centerIndex, index, itemString ->
                                firstCenterValue = centerIndex
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    Text(
                                        modifier = Modifier.padding(vertical = 10.dp),
                                        text = itemString,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = if (centerIndex  != index){
                                            TextSecondary
                                        }else{
                                            TextPrimary
                                        },
                                        textAlign = TextAlign.Center
                                    )
                                    Text(
                                        text = stringResource(R.string.ft_in_ft),
                                        style = MaterialTheme.typography.titleMedium,
                                        color = if (centerIndex == index){
                                            TextPrimary
                                        }else{
                                            Color.Transparent
                                        }

                                    )
                                }
                            }
                        )

                        WheelPicker(
                            modifier = Modifier.weight(1f),
                            listState = secondListState,
                            items = secondItems,
                            onValueChanged = onSecondItemChange,
                            itemContent = { centerIndex, index, itemString ->
                                secondCenterValue = centerIndex
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    Text(
                                        modifier = Modifier
                                            .height(44.dp)
                                            .padding(vertical = 10.dp),
                                        text = itemString,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = if (centerIndex  != index){
                                            TextSecondary
                                        }else{
                                            TextPrimary
                                        },
                                        textAlign = TextAlign.Center
                                    )
                                    Text(
                                        text = stringResource(R.string.ft_in_in),
                                        style = MaterialTheme.typography.titleMedium,
                                        color = if (centerIndex == index){
                                            TextPrimary
                                        }else{
                                            Color.Transparent
                                        }
                                    )
                                }
                            }
                        )

                    }else{
                        WheelPicker(
                            listState = listState,
                            items = items,
                            onValueChanged = onFirstItemChange,
                            itemContent = { centerIndex, index , itemString ->
                                firstCenterValue = centerIndex
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(44.dp)
                                        .padding(vertical = 10.dp),
                                    text = itemString,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = if (centerIndex  != index){
                                        TextSecondary
                                    }else{
                                        TextPrimary
                                    },
                                    textAlign = TextAlign.Center
                                )
                            }
                        )
                    }

                }


                Spacer(Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
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
                            val itemString = if (selectedOption.isNeedTwoColumn){
                                items[firstCenterValue] + ftInOfFt + secondItems[secondCenterValue] + ftInOfIn
                            }else{
                                items[firstCenterValue]
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
            value = "170",
            onDismiss = {},
            onOkClick = {},
            onCancelClick = {},
            firstOption = UnitType.Cm,
            secondOption = UnitType.FtIn,
            selectedOption = UnitType.FtIn,
            items = (0..30).map {
                "${145+it}"
            },
            onUnitTypeClick = {},
            secondItems =(0..30).map {
                "${145+it}"
            },
            onFirstItemChange = {},
            onSecondItemChange = {}
        )
    }
}