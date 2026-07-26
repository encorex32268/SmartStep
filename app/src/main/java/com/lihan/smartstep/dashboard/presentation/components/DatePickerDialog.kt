package com.lihan.smartstep.dashboard.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.lihan.smartstep.R
import com.lihan.smartstep.core.presentation.components.SingleValueWheelPicker
import com.lihan.smartstep.core.presentation.design_system.buttons.ButtonType
import com.lihan.smartstep.core.presentation.design_system.buttons.SmartStepButton
import com.lihan.smartstep.core.presentation.ui.theme.SmartStepTheme
import kotlin.time.Clock

@Composable
fun DatePickerDialog(
    onSave: (Long) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    val datePickerState = rememberDatePickerState(
        initialEpochMillis = Clock.System.now().toEpochMilliseconds()
    )

    Dialog(
        onDismissRequest = onCancel
    ){
        DatePicker(
            modifier = modifier,
            state = datePickerState,
            onSave = onSave,
            onCancel = onCancel
        )

    }

}

@Composable
private fun DatePicker(
    state: DatePickerState,
    onSave: (Long) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
    itemHeight: Dp = 44.dp
){
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(28.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                modifier = Modifier.padding(24.dp),
                text = stringResource(R.string.date),
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
            Box(
                modifier = Modifier.fillMaxWidth()
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    SingleValueWheelPicker(
                        modifier = Modifier.weight(1f),
                        value = state.selectedYear.toString(),
                        items = state.yearList,
                        onValueChange = {
                            state.onYearSelected(it)
                        }
                    )
                    SingleValueWheelPicker(
                        modifier = Modifier.weight(1f),
                        value = state.selectedMonth.toString(),
                        items = state.monthList,
                        onValueChange = state::onMonthSelected
                    )
                    SingleValueWheelPicker(
                        modifier = Modifier.weight(1f),
                        value = state.selectedDay.toString(),
                        items = state.dayList,
                        onValueChange = state::onDaySelected
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp)
                    .padding(end = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
            ) {
                SmartStepButton(
                    text = stringResource(R.string.cancel),
                    onClick = onCancel,
                    type = ButtonType.Text
                )
                SmartStepButton(
                    text = stringResource(R.string.save),
                    onClick = { onSave(state.getSelectedEpochMillis()) },
                    type = ButtonType.Text
                )
            }
        }
    }

}


@Preview(showBackground = true)
@Composable
private fun DatePickerDialogPreview() {
    SmartStepTheme {
        DatePicker(
            state = rememberDatePickerState(
                initialEpochMillis = Clock.System.now().toEpochMilliseconds()
            ),
            onCancel = {},
            onSave = {}
        )
    }
}