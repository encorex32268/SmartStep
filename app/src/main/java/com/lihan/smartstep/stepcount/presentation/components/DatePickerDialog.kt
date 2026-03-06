@file:OptIn(ExperimentalMaterial3Api::class)

package com.lihan.smartstep.stepcount.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lihan.smartstep.R
import com.lihan.smartstep.core.presentation.components.AdaptiveModal
import com.lihan.smartstep.core.presentation.components.WheelPicker
import com.lihan.smartstep.core.presentation.design_system.SmartStepTextButton
import com.lihan.smartstep.core.presentation.modifier.negativePadding
import com.lihan.smartstep.stepcount.presentation.utils.DateTimeUtils
import com.lihan.smartstep.ui.theme.BackgroundTertiary
import com.lihan.smartstep.ui.theme.SmartStepTheme
import com.lihan.smartstep.ui.theme.TextPrimary
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

fun String.withZero(): String{
    val value = this.toIntOrNull()?:return this
    return if (value < 10){
        "0${this}"
    }else{
        this
    }
}

@Composable
fun DatePickerDialog(
    onSaveClick: (Long) -> Unit,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var year by rememberSaveable {
        mutableStateOf(DateTimeUtils.getThisYear())
    }
    var month by rememberSaveable {
        mutableStateOf(DateTimeUtils.getThisMonth())
    }

    var dayRange = rememberSaveable(year,month) {
        DateTimeUtils.getDayRange(year.toInt(),month.toInt()).map { it.toString() }

    }

    var day  = rememberSaveable(dayRange){
        DateTimeUtils.getToday()
    }

    LaunchedEffect(year,month) {
        dayRange = DateTimeUtils.getDayRange(year.toInt(),month.toInt()).map { it.toString() }
    }

    AdaptiveModal(
        modifier = modifier,
        onDismiss = {},
        isDialogLayout = true,
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, bottom = 20.dp)
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    text = stringResource(R.string.date),
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )
                Spacer(Modifier.height(24.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .padding(bottom = 44.dp)
                            .fillMaxWidth()
                            .height(44.dp)
                            .background(color = BackgroundTertiary)
                            .align(Alignment.BottomStart)
                            .negativePadding(horizontal = 24.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        WheelPicker(
                            modifier = Modifier.fillMaxWidth().weight(1f),
                            items = DateTimeUtils.yearRange.map { it.toString() },
                            value = year,
                            onValueChange = {
                                year = it.toString()
                            }
                        )
                        Spacer(Modifier.weight(1f))
                        WheelPicker(
                            modifier = Modifier.fillMaxWidth().weight(1f),
                            items = DateTimeUtils.monthRange.map { it.toString() },
                            value = month,
                            onValueChange = {
                                month = it.toString()
                            }
                        )
                        Spacer(Modifier.weight(1f))
                        WheelPicker(
                            modifier = Modifier.fillMaxWidth().weight(1f),
                            items = dayRange,
                            value = day,
                            onValueChange = {
                                day = (it + 1).toString()
                            }
                        )

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
                        text = stringResource(R.string.save),
                        onClick = {
                            val now = LocalDateTime.now()
                            val year = year.toIntOrNull()?:now.year
                            val month = if (month.toIntOrNull() != null){
                                month.toInt() +  1
                            }else{
                                now.monthValue
                            }
                            val day = day.toIntOrNull()?:now.dayOfMonth

                            val timestamp = LocalDateTime
                                .of(year,month,day,0,0)
                                .atZone(ZoneId.systemDefault())
                                .toInstant().epochSecond

                            onSaveClick(timestamp)
                        }
                    )
                }

            }
        }
    )

}


@Preview(showBackground = true)
@Composable
private fun DatePickerDialogPreview() {
    SmartStepTheme {
        DatePickerDialog(
            onSaveClick = {},
            onCancelClick = {}
        )
    }
}