package com.lihan.smartstep.dashboard.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lihan.smartstep.R
import com.lihan.smartstep.core.presentation.ui.theme.ProgressGreen
import com.lihan.smartstep.core.presentation.ui.theme.SmartStepTheme
import com.lihan.smartstep.dashboard.presentation.model.DailyStepUi
import java.text.NumberFormat

@Composable
fun DailyStepsCard(
    dailySteps: List<DailyStepUi>,
    modifier: Modifier = Modifier
) {
    val average = remember(dailySteps) {
        val sum = dailySteps.sumOf { it.steps}
        NumberFormat.getNumberInstance().format((sum/7))
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.daily_average_steps,average),
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.secondary
                )
            )
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                dailySteps.forEach { dailyStepUi ->
                    DailyItem(
                        modifier = Modifier.weight(1f),
                        dailyStepUi = dailyStepUi
                    )
                }
            }
        }
    }
}


@Composable
private fun DailyItem(
    dailyStepUi: DailyStepUi,
    modifier: Modifier = Modifier
){
    val density = LocalDensity.current
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Canvas(
            modifier = Modifier.size(40.dp)
        ){
            val strokeWidth = with(density){ 4.dp.toPx() }
            val innerStrokeWidth = with(density){ 2.dp.toPx() }

            drawCircle(
                color = Color.White,
                radius = size.minDimension / 2 - strokeWidth / 2,
                style = Stroke(width = strokeWidth)
            )
            val startAngle = -90f
            val progress = dailyStepUi.stepGoalProgress

            val sweepAngle = 360f * progress
            if (progress != 0f){
                drawArc(
                    color = ProgressGreen,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    style = Stroke(
                        width = innerStrokeWidth,
                        cap = StrokeCap.Round
                    ),
                    topLeft = Offset(
                        (size.width - size.minDimension + strokeWidth) / 2,
                        (size.height - size.minDimension + strokeWidth) / 2
                    ),
                    size = Size(
                        size.minDimension - strokeWidth,
                        size.minDimension - strokeWidth
                    )
                )

            }
        }
        Spacer(Modifier.height(4.dp))
        Text(
            text = dailyStepUi.day,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        )
        Text(
            text = dailyStepUi.displaySteps,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.secondary
            )
        )

    }
}


@Preview(showBackground = true)
@Composable
private fun DailyStepsCardPreview() {
    SmartStepTheme {
        DailyStepsCard(
            dailySteps = listOf(
                DailyStepUi(day = "Sun", steps = 15000, stepsGoal = 30000),
                DailyStepUi(day = "Mon", steps = 15000, stepsGoal = 30000),
                DailyStepUi(day = "Tue", steps = 0, stepsGoal = 30000),
                DailyStepUi(day = "Wed", steps = 0, stepsGoal = 30000),
                DailyStepUi(day = "Thu", steps = 0, stepsGoal = 30000),
                DailyStepUi(day = "Fri", steps = 15000, stepsGoal = 30000),
                DailyStepUi(day = "Sat", steps = 15000, stepsGoal = 30000)
            )
        )
    }
}