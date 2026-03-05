package com.lihan.smartstep.stepcount.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lihan.smartstep.ui.theme.AdditionalGreen
import com.lihan.smartstep.ui.theme.BackgroundWhite
import com.lihan.smartstep.ui.theme.ButtonSecondary
import com.lihan.smartstep.ui.theme.SmartStepTheme
import com.lihan.smartstep.ui.theme.TextWhite
import com.lihan.smartstep.ui.theme.medium
import com.lihan.smartstep.ui.theme.regular

@Composable
fun DailyStep(
    steps: String,
    percentage: Float,
    date: String,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Canvas(modifier = Modifier.size(40.dp)) {
            val center = Offset(size.width / 2, size.height / 2)
            val radius = (size.minDimension - 4.dp.toPx()) / 2
            drawCircle(
                color = BackgroundWhite,
                radius = radius,
                center = center,
                style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
            )
            drawArc(
                color = AdditionalGreen,
                startAngle = -90f,
                sweepAngle = 360f * percentage,
                useCenter = false,
                topLeft = Offset(center.x - radius, center.y - radius),
                size = Size(radius * 2, radius * 2),
                style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = date,
            style = MaterialTheme.typography.medium,
            color = TextWhite
        )
        Text(
            text = steps,
            style = MaterialTheme.typography.regular,
            color = ButtonSecondary
        )
    }

}

@Preview
@Composable
private fun DailyStepPreview() {
    SmartStepTheme {
        DailyStep(
            date = "Sun",
            steps = "12,345",
            percentage = 0.3f
        )
    }
}