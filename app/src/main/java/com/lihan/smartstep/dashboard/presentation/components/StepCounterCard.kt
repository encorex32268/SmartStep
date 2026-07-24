@file:OptIn(ExperimentalMaterial3Api::class)

package com.lihan.smartstep.dashboard.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lihan.smartstep.core.presentation.AppIcons
import com.lihan.smartstep.core.presentation.ui.theme.SmartStepTheme
import com.lihan.smartstep.core.presentation.ui.theme.titleAccent

@Composable
fun StepCounterCard(
    currentSteps: Long,
    modifier: Modifier = Modifier,
    goalSteps: Long = 6000
) {
    val progressAnimate by animateFloatAsState(
        targetValue = currentSteps / goalSteps.toFloat(),
        animationSpec = tween(
            durationMillis = 300,
            delayMillis = 300
        )
    )

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
            Surface(
                modifier = Modifier.size(44.dp),
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f)
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ){
                    Icon(
                        imageVector = AppIcons.Sneakers,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
            Text(
                text = currentSteps.toString(),
                style = MaterialTheme.typography.titleAccent.copy(
                    color = MaterialTheme.colorScheme.onPrimary
                )
            )
            Text(
                text = "/${goalSteps} Steps",
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
                        color =  MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f),
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


        }
    }


}


@Preview(showBackground = true)
@Composable
private fun StepCounterCardPreview() {
    SmartStepTheme {
        StepCounterCard(
            currentSteps = 4523
        )
    }
}