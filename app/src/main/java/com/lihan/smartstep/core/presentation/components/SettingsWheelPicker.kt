package com.lihan.smartstep.core.presentation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lihan.smartstep.core.data.model.HeightUnit
import com.lihan.smartstep.core.presentation.ui.theme.SmartStepTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SettingsWheelPicker(
    data: List<WheelPickerData>,
    onValue1Change: (String) -> Unit,
    modifier: Modifier = Modifier,
    itemHeight: Dp = 44.dp,
    onValue2Change: ((String) -> Unit)?=null,
) {


    val firstData = remember(data){ data.first()  }
    val secondData = remember(data){ data.getOrNull(1) }

    val isSingleWheelPick = secondData?.items?.isEmpty()?:true

    Box(
        modifier = modifier
            .fillMaxWidth(),
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeight)
                .offset(y = itemHeight * 2)
                .background(
                    color =  MaterialTheme.colorScheme.surfaceVariant
                )
        )

        if (isSingleWheelPick){
            SingleValueWheelPicker(
                value = firstData.value,
                items = firstData.items,
                itemHeight = itemHeight,
                onValueChange = onValue1Change
            )
        }else{
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SingleValueWheelPicker(
                    modifier = Modifier.weight(2f),
                    value = firstData.value,
                    items = firstData.items,
                    itemHeight = itemHeight,
                    onValueChange = onValue1Change
                )
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .offset(y = itemHeight / 2),
                    text = firstData.unit,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    ),
                )
                SingleValueWheelPicker(
                    modifier = Modifier.weight(2f),
                    value = secondData.value,
                    items = secondData.items,
                    itemHeight = itemHeight,
                    onValueChange = {
                        onValue2Change?.invoke(it)
                    }
                )
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .offset(y = itemHeight / 2),
                    text = secondData.unit,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    ),
                )
            }


        }

    }


}

@Preview(showBackground = true)
@Composable
private fun SingleValueWheelPickerPreview() {
    SmartStepTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){

            SettingsWheelPicker(
                data = listOf(
                    WheelPickerData(
                        value = "170",
                        items = (120..220).map { it.toString() }
                    )
                ),
                onValue1Change = {}
            )
            Spacer(Modifier.height(20.dp))
            SettingsWheelPicker(
                data = listOf(
                    WheelPickerData(
                        value = "5",
                        items = (0..10).map { it.toString() },
                        unit = "ft"
                    ),
                    WheelPickerData(
                        value = "7",
                        items = (0..10).map { it.toString() },
                        unit = "in"
                    ),
                ),
                onValue1Change = {},
                onValue2Change = {}
            )
        }
    }
}


