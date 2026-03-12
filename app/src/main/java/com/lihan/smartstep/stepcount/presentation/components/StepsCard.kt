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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lihan.smartstep.R
import com.lihan.smartstep.core.presentation.design_system.Clock
import com.lihan.smartstep.core.presentation.design_system.Direction
import com.lihan.smartstep.core.presentation.design_system.Pause
import com.lihan.smartstep.core.presentation.design_system.PenEdit
import com.lihan.smartstep.core.presentation.design_system.Play
import com.lihan.smartstep.core.presentation.design_system.SmartStepIconButton
import com.lihan.smartstep.core.presentation.design_system.Sneaker
import com.lihan.smartstep.core.presentation.design_system.WeightDiet
import com.lihan.smartstep.stepcount.presentation.formatThousands
import com.lihan.smartstep.ui.theme.BackgroundWhite20
import com.lihan.smartstep.ui.theme.ButtonPrimary
import com.lihan.smartstep.ui.theme.SmartStepTheme
import com.lihan.smartstep.ui.theme.TextWhite
import com.lihan.smartstep.ui.theme.titleAccent
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Composable
fun StepsCard(
    isCounting: Boolean,
    steps: Long,
    stepsTotal: Long,
    distance: Double,
    calories: Long,
    timer: Int,
    onPauseClick: () -> Unit,
    onResumeClick: () -> Unit,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = ButtonPrimary,
        shape = RoundedCornerShape(24.dp),
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SmartStepIconButton(
                    imageVector = Sneaker,
                    enabled = false,
                )
                Spacer(Modifier.weight(1f))
                SmartStepIconButton(
                    imageVector = PenEdit,
                    shape = CircleShape,
                    onClick = onEditClick
                )
                Spacer(Modifier.width(16.dp))
                SmartStepIconButton(
                    imageVector = if (isCounting){
                        Pause
                    }else{
                        Play
                    },
                    shape = CircleShape,
                    onClick = {
                        if (isCounting){
                            onPauseClick()
                        }else{
                            onResumeClick()
                        }
                    }
                )

            }

            Spacer(Modifier.height(16.dp))
            Text(
                text = steps.formatThousands(),
                style = MaterialTheme.typography.titleAccent,
                color = if (isCounting){
                    TextWhite
                }else{
                    BackgroundWhite20
                }
            )
            Text(
                text = if (isCounting){
                    stringResource(R.string.total_steps,stepsTotal)
                }else{
                    stringResource(R.string.paused)
                },
                style = MaterialTheme.typography.titleMedium,
                color = TextWhite.copy(alpha = 0.9f)
            )
            Spacer(Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(100))
                    .fillMaxWidth()
                    .height(12.dp)
                    .background(color = BackgroundWhite20),
                contentAlignment = Alignment.CenterStart
            ){
                Slider(
                    modifier = Modifier
                        .padding(bottom = 0.5.dp),
                    value = (steps.toFloat() / stepsTotal.toFloat()),
                    onValueChange = {},
                    track = {
                        SliderDefaults.Track(
                            modifier = Modifier
                                .height(8.dp),
                            sliderState = it,
                            drawStopIndicator = null,
                            trackInsideCornerSize = 4.dp,
                            thumbTrackGapSize = 0.dp,
                            colors = SliderDefaults.colors(
                                activeTickColor = Color.Red,
                                disabledActiveTrackColor = Color.Red,
                                activeTrackColor = TextWhite,
                                inactiveTrackColor = Color.Transparent
                            ),
                        )
                    },
                    thumb = {},
                    colors = SliderDefaults.colors(
                        activeTickColor = Color.Red,
                        disabledActiveTrackColor = Color.Red
                    )
                )

            }
            Spacer(Modifier.height(32.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    SmartStepIconButton(
                        imageVector = Direction,
                        enabled = false
                    )
                    CounterElement(
                        value = distance.toString(),
                        unit = stringResource(R.string.km)
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    SmartStepIconButton(
                        imageVector = WeightDiet,
                        onClick = { }
                    )
                    CounterElement(
                        value = calories.toString(),
                        unit = stringResource(R.string.kcal)
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    SmartStepIconButton(
                        imageVector = Clock,
                        onClick = { }
                    )
                    CounterElement(
                        value = timer.toString(),
                        unit = stringResource(R.string.min)
                    )
                }

            }



        }

    }

}


@Preview(showBackground = true)
@Composable
private fun StepsCardPreview() {
    SmartStepTheme {
        StepsCard(
            isCounting = false,
            steps = 5423,
            stepsTotal = 6000,
            onPauseClick = {},
            onResumeClick = {},
            onEditClick = {},
            distance = 1.23,
            calories = 304,
            timer = 120
        )
    }
}