@file:OptIn(ExperimentalMaterial3Api::class)

package com.lihan.smartstep.dashboard.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSliderState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lihan.smartstep.R
import com.lihan.smartstep.core.presentation.AppIcons
import com.lihan.smartstep.core.presentation.design_system.buttons.IconButtonSize
import com.lihan.smartstep.core.presentation.design_system.buttons.SmartStepIconButton
import com.lihan.smartstep.core.presentation.ui.theme.SmartStepTheme
import com.lihan.smartstep.core.presentation.ui.theme.titleAccent
import com.lihan.smartstep.core.presentation.util.toFormattedTime
import com.lihan.smartstep.core.presentation.util.toNumberString

@Composable
fun StepCounterCard(
    isTracking: Boolean,
    currentSteps: Int,
    distance: String,
    kcal: Int,
    time: String,
    goalSteps: Int,
    onStopTracking: () -> Unit,
    onStartTracking: () -> Unit,
    modifier: Modifier = Modifier
) {
    val progressAnimate by animateFloatAsState(
        targetValue = currentSteps / goalSteps.toFloat(),
        animationSpec = tween(
            durationMillis = 700,
            delayMillis = 300
        )
    )

    val goalStepsText =  if (isTracking){
        stringResource(R.string.goldsteps, goalSteps)
    }else{
        stringResource(R.string.paused)
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.primary,
        contentColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SmartStepIconButton(
                    imageVector = AppIcons.Sneakers,
                    contentDescription = null
                )
                Spacer(Modifier.weight(1f))
                SmartStepIconButton(
                    imageVector = AppIcons.Edit,
                    contentDescription = null,
                    shape = CircleShape
                )
                SmartStepIconButton(
                    imageVector = if (isTracking){ AppIcons.Pause } else { AppIcons.Play },
                    contentDescription = null,
                    shape = CircleShape,
                    onClick = {
                        if (isTracking){
                            onStopTracking()
                        }else{
                            onStartTracking()
                        }
                    }
                )
            }

            Spacer(Modifier.height(16.dp))
            Text(
                modifier = Modifier
                    .graphicsLayer{
                        alpha = if (isTracking){
                            1f
                        }else 0.2f
                    },
                text = currentSteps.toNumberString(),
                style = MaterialTheme.typography.titleAccent.copy(
                    color = MaterialTheme.colorScheme.onPrimary
                )
            )
            Text(
                text = goalStepsText,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.onPrimary
                )
            )
            Spacer(Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(100))
                    .background(
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(100)
                    )
                    .height(12.dp),
                contentAlignment = Alignment.CenterStart
            ){
                Slider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
                    value = progressAnimate,
                    onValueChange = {},
                    thumb = {},
                    track = { sliderState ->
                        SliderDefaults.Track(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(100.dp)) //for start sharp
                                .height(8.dp),
                            drawStopIndicator = null,
                            sliderState = sliderState,
                            thumbTrackGapSize = 0.dp,
                            trackInsideCornerSize = 100.dp, //for end sharp
                            colors = SliderDefaults.colors(
                                activeTrackColor = Color.White,
                                inactiveTrackColor = Color.Transparent
                            )
                        )
                    },
                    valueRange = 0f..1f
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
                        modifier = Modifier.padding(horizontal = 4.dp),
                        iconButtonSize = IconButtonSize.MEDIUM,
                        imageVector = AppIcons.LocationDirection,
                        contentDescription = null
                    )
                    UnitText(
                        text = distance,
                        unit = "km"
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    SmartStepIconButton(
                        iconButtonSize = IconButtonSize.MEDIUM,
                        imageVector = AppIcons.Weight,
                        contentDescription = null,
                    )
                    UnitText(
                        text = kcal.toNumberString(),
                        unit = "kcal"
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    SmartStepIconButton(
                        iconButtonSize = IconButtonSize.MEDIUM,
                        imageVector = AppIcons.TimeClock,
                        contentDescription = null,
                    )
                    UnitText(
                        text = time,
                        unit = "min"
                    )
                }
            }

        }
    }


}


@Preview(showBackground = true)
@Composable
private fun StepCounterCardPreview() {
    SmartStepTheme {
        StepCounterCard(
            currentSteps = 4523,
            isTracking = false,
            onStopTracking = {},
            onStartTracking = {},
            distance = "4.7",
            kcal = 1230,
            time = "42",
            goalSteps = 2000
        )
    }
}