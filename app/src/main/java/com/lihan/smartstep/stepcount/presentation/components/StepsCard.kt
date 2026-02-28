@file:OptIn(ExperimentalMaterial3Api::class)

package com.lihan.smartstep.stepcount.presentation.components

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
import com.lihan.smartstep.core.presentation.design_system.Sneaker
import com.lihan.smartstep.ui.theme.BackgroundWhite20
import com.lihan.smartstep.ui.theme.ButtonPrimary
import com.lihan.smartstep.ui.theme.SmartStepTheme
import com.lihan.smartstep.ui.theme.TextWhite
import com.lihan.smartstep.ui.theme.titleAccent

@Composable
fun StepsCard(
    steps: String,
    stepsTotal: String,
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
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .size(38.dp)
                    .background(color = BackgroundWhite20),
                contentAlignment = Alignment.Center
            ){
                Icon(
                    imageVector = Sneaker,
                    contentDescription = null,
                    tint = TextWhite
                )
            }
            Spacer(Modifier.height(16.dp))
            Text(
                text = steps,
                style = MaterialTheme.typography.titleAccent,
                color = TextWhite
            )
            Text(
                text = stringResource(R.string.total_steps,stepsTotal),
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
                    value = 0.3f,
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

        }

    }

}


@Preview(showBackground = true)
@Composable
private fun StepsCardPreview() {
    SmartStepTheme {
        StepsCard(
            steps = "4,523",
            stepsTotal = "6000"
        )
    }
}